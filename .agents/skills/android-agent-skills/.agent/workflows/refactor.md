---
description: Refactor legacy code to modern patterns
---

# Refactoring Workflow

Use this workflow when refactoring legacy Android code to modern patterns.

## Prerequisites
- [ ] Identify current patterns in use
- [ ] Read `skills/guides/27-refactoring-patterns.md`

## Common Refactoring Paths

### Path 1: LiveData → StateFlow

```kotlin
// ❌ BEFORE: LiveData
class OldViewModel : ViewModel() {
    private val _data = MutableLiveData<Data>()
    val data: LiveData<Data> = _data
}

// ✅ AFTER: StateFlow
class NewViewModel : ViewModel() {
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()
}
```

**Steps:**
1. Replace `MutableLiveData<T>` with `MutableStateFlow<T>`
2. Replace `LiveData<T>` with `StateFlow<T>`
3. Add `.asStateFlow()` to expose read-only
4. In Compose: `collectAsStateWithLifecycle()` instead of `observeAsState()`

---

### Path 2: SharedFlow Events → Channel Events

```kotlin
// ❌ BEFORE: SharedFlow (can lose events!)
private val _events = MutableSharedFlow<Event>()
val events = _events.asSharedFlow()

// ✅ AFTER: Channel (guaranteed delivery)
private val _events = Channel<Event>()
val events = _events.receiveAsFlow()
```

---

### Path 3: Repository in ViewModel → UseCase

```kotlin
// ❌ BEFORE: Direct repository injection
@HiltViewModel
class OldViewModel @Inject constructor(
    private val userRepository: UserRepository,  // ❌
    private val productRepository: ProductRepository,  // ❌
) : ViewModel()

// ✅ AFTER: UseCase injection
@HiltViewModel
class NewViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,  // ✅
    private val getProductsUseCase: GetProductsUseCase,  // ✅
) : ViewModel()
```

**Steps:**
1. Create UseCase for each repository method used
2. Move business logic from ViewModel to UseCase
3. Replace repository injection with UseCase injection
4. Update tests to mock UseCases

---

### Path 4: XML → Compose

**Steps:**
1. Start with leaf components (no children)
2. Create stateless @Composable for each view
3. Add modifier parameter as last argument
4. Test with @Preview
5. Work up to screens

```kotlin
// From XML Button to Compose
@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
    ) {
        Text(text)
    }
}
```

---

### Path 5: Hilt ↔ Koin

#### Hilt → Koin
```kotlin
// BEFORE: Hilt
@HiltViewModel
class MyViewModel @Inject constructor(
    private val useCase: MyUseCase,
) : ViewModel()

// AFTER: Koin
class MyViewModel(
    private val useCase: MyUseCase,
) : ViewModel()

// Koin module
val featureModule = module {
    viewModelOf(::MyViewModel)
    singleOf(::MyUseCase)
}
```

#### Koin → Hilt
```kotlin
// BEFORE: Koin
class MyViewModel(useCase: MyUseCase) : ViewModel()

// AFTER: Hilt
@HiltViewModel
class MyViewModel @Inject constructor(
    private val useCase: MyUseCase,
) : ViewModel()

// Hilt module
@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun bindUseCase(impl: MyUseCaseImpl): MyUseCase
}
```

---

## Refactoring Checklist

Before submitting refactored code:

- [ ] All tests pass
- [ ] No LiveData in new code (use StateFlow)
- [ ] No SharedFlow for events (use Channel)
- [ ] ViewModels inject UseCases, not Repositories
- [ ] All state classes marked @Immutable
- [ ] Compose uses collectAsStateWithLifecycle()
- [ ] No Android imports in domain layer

## Verification

// turbo
```
After refactoring, run:
- @analyze-test-coverage
- @review-changes (if using git)
- Check Architecture compliance
```

---

**Reference**: `skills/guides/27-refactoring-patterns.md` for detailed migration guides
