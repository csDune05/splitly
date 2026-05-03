---
description: Generate a complete feature with all layers
---

# Generate Feature Workflow

Use this workflow when you need to create a **complete Android feature** with all layers (Domain, Data, Presentation).

## Prerequisites
- [ ] Read `skills/AI_CONTEXT.md` for quick context
- [ ] Read `skills/AGENT_SUMMARY.md` for core rules

## Workflow Steps

### Step 1: Analyze Requirements
// turbo
```
Identify:
- Feature name
- Entity/Model fields and types
- CRUD operations needed
- UI states required
- DI framework (Hilt or Koin)
```

### Step 2: Generate Domain Layer

#### 2.1 Create Model
```kotlin
// domain/model/[Entity].kt
@Immutable
data class [Entity](
    val id: String,
    // other fields
)
```

#### 2.2 Generate UseCase
Use prompt: `@gen-usecase [EntityName]`
Reference: `skills/templates/examples/UseCaseViewModelExample.kt`

#### 2.3 Create Repository Interface
```kotlin
// domain/repository/[Entity]Repository.kt
interface [Entity]Repository {
    suspend fun get(id: String): Result<[Entity]>
    fun observe(id: String): Flow<[Entity]>
    suspend fun save(entity: [Entity]): Result<Unit>
}
```

### Step 3: Generate Data Layer

#### 3.1 Generate Repository Implementation
Use prompt: `@gen-repository [EntityName]`
Reference: `skills/templates/examples/UserRepositoryImpl.kt`

#### 3.2 Create DTOs and Mappers
```kotlin
// data/dto/[Entity]Dto.kt
@Serializable
data class [Entity]Dto(...)

// data/mapper/[Entity]Mapper.kt
fun [Entity]Dto.toDomain(): [Entity] = ...
fun [Entity].toDto(): [Entity]Dto = ...
```

### Step 4: Generate Presentation Layer

#### 4.1 Generate ViewModel
Use prompt: `@gen-viewmodel [FeatureName]`
Reference: `skills/templates/examples/UseCaseViewModelExample.kt`

#### 4.2 Generate Screen
Use prompt: `@gen-screen [FeatureName]`
Reference: `skills/templates/compose/ComposeExample.kt`

### Step 5: Setup DI

#### Hilt
```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class [Feature]Module {
    @Binds
    abstract fun bindRepository(impl: [Entity]RepositoryImpl): [Entity]Repository
}
```

#### Koin
```kotlin
val featureModule = module {
    singleOf(::GetEntityUseCase)
    viewModelOf(::FeatureViewModel)
}
```

### Step 6: Generate Tests
// turbo
```
Use prompts:
- @gen-usecase-test [UseCaseName]
- @gen-viewmodel-test [ViewModelName]
- @gen-screen-test [ScreenName]
```

### Step 7: Verify Quality
// turbo
```
Run:
- @analyze-test-coverage
- Check Architecture compliance
- Verify @Immutable annotations
```

## Verification Checklist
- [ ] Domain layer has NO Android imports
- [ ] ViewModel injects UseCase, NOT Repository
- [ ] All state classes marked @Immutable
- [ ] All UseCases return Result<T>
- [ ] Tests cover success, error, edge cases
