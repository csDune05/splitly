# Android Agent Skills

> **Version**: 2.2.0 | **Status**: Production-Ready | **Last Updated**: January 2026  
> **Author**: TrongLB | **License**: MIT

A production-ready, reusable Agent Skill framework for Android Kotlin projects.

## Features

- **Agent Skills Spec Compliant** - Follows [agentskills.io](https://agentskills.io) specification
- **Pure Kotlin** - No Android dependencies in core skills
- **Clean Architecture** - Interface-driven design
- **Coroutines & Flow** - Full async support with cancellation
- **Compose-Ready** - `@Immutable`/`@Stable` annotations for UI stability
- **Validation DSL** - 15+ validation functions with clean DSL
- **DI Friendly** - Compatible with Hilt, Koin, or manual DI (with qualifiers)
- **Fully Testable** - All dependencies injectable
- **AI-Ready** - Comprehensive guides and prompts for AI agents

## 🚀 Installation / Setup

### Option 1: Using Setup Script (Recommended)

Use the interactive setup wizard for automatic installation:

**macOS / Linux / Windows (Git Bash):**
```bash
# Clone the repository
git clone https://github.com/devtrongle/android-agent-skills.git
cd android-agent-skills

# Run setup wizard
./scripts/setup.sh
```

#### Setup Wizard Walkthrough

The wizard will guide you through these steps:

**Step 1: Version Check**
```
🔍 Checking for updates...
   Local version:  2.2.0
   Remote version: 2.2.0
✓ You have the latest version (2.2.0)
```

**Step 2: Target Project Path**
```
📁 Enter target project path:
~/Projects/my-android-app
✓ Target: /Users/username/Projects/my-android-app
```

> 💡 **Tip**: Supports both Unix paths (`~/Projects/app`) and Windows paths (`D:\Projects\app`)

**Step 3: IDE Selection**
```
🔧 Select IDE(s) to configure (comma-separated):
   1) Antigravity
   2) GitHub Copilot

Your choice [1,2 or 1-2]: 1,2
```

| Choice | Description | Files Installed |
|--------|-------------|-----------------|
| `1` | Antigravity only | `.agent/workflows/` |
| `2` | GitHub Copilot only | `.github/copilot-instructions.md` |
| `1,2` or `1-2` | Both IDEs | Both folders |

**Step 4: Replace Existing Files**
```
⚠️  Replace existing config files? (old files will be backed up as .bak)
   [y/N]: y
```

If you choose `y`, existing files will be backed up with `.bak` suffix before replacement.

**Installation Complete:**
```
═══════════════════════════════════════════════════════════
Installation complete!

Installed:
  ✓ Antigravity (.agent/)
  ✓ GitHub Copilot (.github/copilot-instructions.md)
  ✓ Agent Skills (agent-skills/)
```

#### What Gets Installed

| Component | Destination | Description |
|-----------|-------------|-------------|
| `.agent/workflows/` | Target project | AI workflows for Antigravity |
| `.github/copilot-instructions.md` | Target project | GitHub Copilot config |
| `agent-skills/` | Target project | Full skill framework (always replaced with latest) |

> ⚠️ **Note**: The `agent-skills/` folder is always replaced with the latest version. Other files are only replaced if you choose to.

### Option 2: Manual Copy

Copy these folders/files to your Android project root:

```bash
# Clone or download the repository
git clone https://github.com/devtrongle/android-agent-skills.git

# Copy to your project (from the cloned repo)
cp -r .github your-android-project/
cp -r .agent your-android-project/
cp -r . your-android-project/agent-skills/

# Or manually copy:
# ├── .github/           → your-project/.github/
# ├── .agent/            → your-project/.agent/
# └── (entire repo)      → your-project/agent-skills/
```

### Verify Structure

After copying, your project should look like:

```
your-android-project/
├── .github/
│   └── copilot-instructions.md     ✅ AI agent config
├── .agent/
│   └── workflows/                  ✅ AI workflows
│       ├── analyze-skill.md
│       ├── generate-feature.md
│       └── refactor.md
├── agent-skills/
│   ├── SKILL.md                    ✅ Agent Skills entry point (spec-compliant)
│   ├── skills/                     ✅ Framework core
│   │   ├── AI_CONTEXT.md           ✅ Quick reference
│   │   ├── AI_PROMPTS.md
│   │   ├── AGENT_SUMMARY.md
│   │   ├── guides/                 ✅ 36 comprehensive guides
│   │   └── templates/              ✅ Code templates
│   ├── VALIDATION_CHEAT_SHEET.md   ✅ Cheat sheets
│   └── README.md
├── app/                            Your existing code
└── build.gradle.kts
```

### Test AI Integration

Open your project in your IDE with AI assistant enabled (GitHub Copilot, Claude, etc.):

1. Type `/help` → Should show available commands
2. Type `/help-vi` → Should show Vietnamese help
3. Type `@gen-viewmodel UserProfile` → Should generate ViewModel code

### (Optional) Customize

- Edit `.github/copilot-instructions.md` to customize AI behavior
- Add project-specific rules to `skills/AGENT_SUMMARY.md`
- Create custom workflows in `.agent/workflows/`

> 💡 **Tip**: The AI agent will automatically read `copilot-instructions.md` when you start coding!

### 🔄 Updating

When there are updates to the skill framework:

```bash
# Pull latest changes
cd android-agent-skills
git pull

# Re-run setup wizard
./scripts/setup.sh
# Enter your project path and choose "y" to replace existing files
```

> 💡 **Tip**: The wizard automatically checks for updates when you run it!

## Repository Structure

This is the source repository structure:

```
android-agent-skills/
├── .github/                            # AI agent instructions
│   └── copilot-instructions.md         #   GitHub Copilot/AI config
│
├── .agent/                             # AI workflows  
│   └── workflows/                      #   Automated workflows
│       ├── analyze-skill.md
│       ├── generate-feature.md
│       └── refactor.md
│
├── scripts/                            # Installation scripts
│   └── setup.sh                        #   Setup script for macOS/Linux
│
├── skills/                             # Core framework
│   ├── AI_PROMPTS.md                   #   AI code generation prompts
│   ├── AI_CONTEXT.md                   #   Quick reference for AI agents
│   ├── AGENT_SUMMARY.md                #   Core rules & patterns
│   │
│   ├── guides/                         #   36 comprehensive guides
│   │   ├── 00-decision-tree.md         #   Quick navigation
│   │   ├── 01-architecture.md          #   Clean Architecture
│   │   ├── 02-coding-conventions.md
│   │   ├── 03-coroutines-concurrency.md
│   │   ├── 04-project-structure.md
│   │   ├── 05-jetpack-compose.md
│   │   ├── 06-compose-performance.md
│   │   ├── 07-state-management.md
│   │   ├── 08-dependency-injection.md
│   │   ├── 09-version-catalog.md
│   │   ├── 10-error-handling.md
│   │   ├── 11-testing.md
│   │   ├── 12-security.md
│   │   ├── 13-navigation.md
│   │   ├── 14-offline-first.md
│   │   ├── 15-kmp-readiness.md
│   │   ├── 16-compose-multiplatform.md
│   │   ├── 17-build-configuration.md
│   │   ├── 18-ci-cd.md
│   │   ├── 19-accessibility.md
│   │   ├── 20-anti-patterns.md
│   │   ├── 21-code-review.md
│   │   ├── 22-checklists.md
│   │   ├── 23-memory-performance.md
│   │   ├── 24-validation-rules.md
│   │   ├── 25-performance-benchmarks.md
│   │   ├── 26-context-aware-suggestions.md
│   │   ├── 27-refactoring-patterns.md
│   │   ├── 28-code-generation.md
│   │   ├── 29-testing-automation.md
│   │   ├── 30-skill-analysis.md
│   │   ├── 31-pipeline-patterns.md
│   │   ├── 32-modern-kotlin-features.md
│   │   ├── 33-agent-feedback-loop.md
│   │   ├── 34-logging-analytics.md
│   │   ├── 35-gradle-optimization.md
│   │   └── README.md
│   │
│   └── templates/                      #   Code templates
│       ├── skill/                      #   Core skill abstractions
│       │   ├── base/                   #   Skill, SkillResult, Validation DSL
│       │   ├── registry/               #   Skill registration
│       │   ├── executor/               #   Execution engine
│       │   └── wrapper/                #   Retry, timeout wrappers
│       ├── skills/                     #   Built-in skills
│       │   ├── di/                     #   Koin validation, generation
│       │   ├── intent/                 #   Intent detection
│       │   ├── memory/                 #   Memory read/write
│       │   └── fallback/               #   Fallback handling
│       ├── testing/                    #   Test templates
│       ├── controller/                 #   Agent controller
│       ├── pipeline/                   #   Pipeline builder
│       ├── di/                         #   Hilt/Koin setup
│       ├── compose/                    #   Compose integration
│       ├── examples/                   #   Usage examples
│       ├── memory/                     #   Memory store
│       └── util/                       #   Utilities
│
├── SKILL.md                            # Agent Skills entry point (spec-compliant)
├── ARCHITECTURE.md                     # Complete architecture documentation
├── VALIDATION_CHEAT_SHEET.md           # Validation functions quick reference
├── ERROR_HANDLING_CHEAT_SHEET.md       # Error patterns & exception mapping
├── TESTING_CHEAT_SHEET.md              # MockK, Turbine, coroutine testing
├── COMPOSE_CHEAT_SHEET.md              # Compose patterns & best practices
└── README.md                           # This file
```

## 🤖 AI Agent Commands

### Mode Activation
AI agents (GitHub Copilot, Claude, etc.) work in **flexible mode** by default. Use these commands to activate specific modes:

| Command | Mode | When to use |
|---------|------|-------------|
| `/skill` or `@gen-skill` | Skill Mode | Creating/editing Agent Skills |
| `/android` or `@gen-*` | Android Mode | Android code (ViewModel, Repository, etc.) |
| `/rules` or `/guide` | Show Rules | List all available guides & rules |
| `/help` | Help (English) | Show available commands |
| `/help-vi` | Trợ giúp (Tiếng Việt) | Hiển thị các lệnh có sẵn |

### Helper Commands (Quick Access)
| Command | Purpose | Shows |
|---------|---------|-------|
| `/context` | Load Context | AI_CONTEXT.md - Quick reference for AI |
| `/summary` | Core Rules | AGENT_SUMMARY.md - Architecture & patterns |
| `/cheat` | Cheat Sheets | List all available cheat sheets |
| `/templates` | Templates | Available code templates in skills/templates/ |
| `/examples` | Examples | Example files for reference |
| `/decision` | Decision Tree | Quick navigation guide (00-decision-tree.md) |

### Pattern Quick Reference
| Command | Topic | Reference |
|---------|-------|-----------| 
| `/validate` | Validation | Validation DSL functions & patterns |
| `/errors` | Error Handling | Exception mapping & error patterns |
| `/test` | Testing | MockK, Turbine, coroutine testing |
| `/compose` | Compose | Compose patterns & best practices |
| `/nav` | Navigation | Type-safe navigation patterns |
| `/di` | Dependency Injection | Hilt & Koin setup patterns |
| `/flow` | State Management | StateFlow, Channel, MVI patterns |
| `/offline` | Offline-First | Caching & sync patterns |
| `/clean` | Clean Code | Anti-patterns & code quality rules |

### Code Generation Commands
```
# Android Components
@gen-viewmodel [name]    Generate ViewModel (MVI pattern)
@gen-repository [name]   Generate Repository (offline-first)
@gen-usecase [name]      Generate UseCase (business logic)
@gen-screen [name]       Generate Compose Screen (Route/Screen pattern)
@gen-feature [name]      Generate complete feature (all layers)

# Agent Skills
@gen-skill [name]        Generate Agent Skill (pure Kotlin)
@gen-pipeline [name]     Generate Skill pipeline with error handling
```

### Testing Commands
```
# Generate Tests
@gen-test [component]         Generate tests (auto-detect type)
@gen-viewmodel-test [name]    Generate ViewModel tests (Turbine + MockK)
@gen-usecase-test [name]      Generate UseCase tests with validation
@gen-repository-test [name]   Generate Repository tests (offline/online)
@gen-screen-test [name]       Generate Compose UI tests
@gen-integration-test [name]  Generate multi-layer integration tests
@gen-skill-test [name]        Generate Agent Skill tests
```

### Analysis & Quality Commands
```
# Analysis
@analyze-skill           Analyze skill quality (validation, errors, docs)
@analyze-test-coverage   Find untested code and gaps
@analyze-test-quality    Validate test quality and detect smells

# Optimization
@optimize-skill          Optimize skill performance (algorithm, memory)
@add-validation          Generate comprehensive validation for inputs
```

### Review Commands
```
@review                  Review current file for issues
@review-changes          Review uncommitted changes
```

### Refactoring & Migration Commands (via /refactor workflow)
```
/refactor                Start refactoring workflow
  - LiveData → StateFlow migration
  - SharedFlow → Channel migration  
  - Repository → UseCase refactoring
  - XML → Compose migration
  - Hilt ↔ Koin migration
```

### Auto-Activation
AI agents automatically activate the appropriate mode when:
- Editing files in `skills/templates/` → Skill Mode
- Editing `*ViewModel.kt`, `*Repository.kt`, `*UseCase.kt` → Android Mode
- Using `@gen-*` prompts → Corresponding mode

> 📖 Full prompts: [skills/AI_PROMPTS.md](skills/AI_PROMPTS.md)

## Quick Start

### 1. Manual DI Setup

```kotlin
// Create the module
val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
val agentCore = AgentCoreModule.createDefault(scope)

// Get the controller
val controller = agentCore.agentController

// Execute a skill
val result = controller.executeSkill<IntentDetectInput, IntentDetectOutput>(
    skillId = "core.intent.detect",
    input = IntentDetectInput(text = "Hello, how can you help?"),
)
```

### 2. Creating a Custom Skill

```kotlin
// Use @Immutable for Compose stability
@Immutable
data class MyInput(val query: String, val limit: Int = 10)

@Immutable
data class MyOutput(val result: String, val confidence: Float)

class MyDomainSkill : BaseSkill<MyInput, MyOutput>() {
    
    override val metadata = skillMetadata {
        id = "domain.my_skill"
        name = "My Domain Skill"
        description = "Does something specific to my domain"
        category = SkillCategory.DOMAIN
    }
    
    // Use the validation DSL for clean validation
    override suspend fun validate(input: MyInput): SkillResult<Unit> {
        return validate {
            requireNotBlank(input.query, "query")
            requireInRange(input.limit, 1..100, "limit")
        }
    }
    
    override suspend fun doExecute(
        input: MyInput,
        context: SkillContext,
    ): SkillResult<MyOutput> {
        checkCancellation() // Support cooperative cancellation
        // Your logic here
        return SkillResult.success(MyOutput(...))
    }
}
```

### 3. Building a Pipeline

```kotlin
val pipeline = pipeline<String>("MyPipeline") {
    description("Processes user input")
    
    step(intentDetectSkill, name = "DetectIntent") {
        inputTransformer = { IntentDetectInput(text = it as String) }
    }
    
    step(processSkill, name = "Process")
    
    step(memoryWriteSkill, name = "SaveResult")
}.returning<MemoryWriteOutput>()

val result = controller.executePipeline(pipeline, "user input")
```

### 4. Compose Integration

```kotlin
@HiltViewModel
class AgentViewModel @Inject constructor(
    private val controller: AgentController,
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    fun processInput(text: String) {
        viewModelScope.launch {
            val result = controller.executeSkill<...>(...)
            // Update state
        }
    }
}
```

## Core Skills

| Skill | ID | Description |
|-------|-----|-------------|
| MemoryReadSkill | `core.memory.read` | Read from memory store |
| MemoryWriteSkill | `core.memory.write` | Write to memory store |
| IntentDetectSkill | `core.intent.detect` | Rule-based intent detection |
| FallbackSkill | `core.fallback` | Graceful fallback handling |
| NetworkStatusCheckSkill | `core.network.status` | Check network connectivity |
| LoggingSkill | `core.logging` | Log messages |
| KoinValidationSkill | `core.di.koin_validation` | Validate Koin DI configuration |
| KoinModuleGeneratorSkill | `core.di.koin_generator` | Generate Koin module code |
| KoinBestPracticesSkill | `core.di.koin_best_practices` | Analyze code for Koin best practices |

## Architecture Principles

1. **Skills have NO Android dependencies** - Pure Kotlin only
2. **All side effects are injected** - Network, storage, logging via interfaces
3. **Skills are single-responsibility** - One focused task per skill
4. **Results are typed** - SkillResult<T> for success/failure with extensions
5. **Everything is testable** - Inject test doubles easily
6. **Compose-ready** - `@Immutable`/`@Stable` annotations for UI stability
7. **Cancellation-aware** - Cooperative cancellation throughout pipelines
8. **Clean validation** - Validation DSL for type-safe input validation

## Adding New Domain Skills

1. Create input/output data classes (with `@Immutable`)
2. Extend `BaseSkill<Input, Output>`
3. Use validation DSL in `validate()`
4. Implement `doExecute()` with cancellation support
5. Register in `SkillRegistry`
6. Write unit tests

See `skills/templates/examples/DomainSkillGuide.kt` for detailed templates.

## New in v2.2

### Enhanced Validation DSL (15+ Functions)
```kotlin
override suspend fun validate(input: MyInput): SkillResult<Unit> {
    return validate {
        // String validation
        requireNotBlank(input.name, "name")
        requireValidEmail(input.email, "email")
        requireValidPassword(input.password, "password")
        
        // Numeric validation
        requireInRange(input.age, 0..150, "age")
        requirePositive(input.count, "count")
        requireValidConfidence(input.confidence, "confidence") // 0.0-1.0
        
        // URL/Phone validation
        requireValidUrl(input.website, "website")
        requireValidPhone(input.phone, "phone")
        
        // Collection validation
        requireNotEmpty(input.items, "items")
        requireSize(input.tags, 1..10, "tags")
    }
}
```

### SkillResult Extensions
```kotlin
// Chain operations
result.andThen { value -> SkillResult.success(transform(value)) }

// Recover from failure
result.recover { error -> defaultValue }

// Combine results
result1.zip(result2) { a, b -> Combined(a, b) }
```

### Flow Utilities
```kotlin
skillFlow
    .withSkillDefaults(dispatchers.io)
    .retryWithBackoff(maxRetries = 3)
    .collect { result -> /* handle */ }
```

### Cancellation Support
```kotlin
// In doExecute()
checkCancellation() // Check before heavy operations

// For iterations
items.mapWithCancellation { processItem(it) }
```

## Testing

```kotlin
@Test
fun `test skill execution`() = runTest {
    val clock = TestClock()
    val dispatchers = TestCoroutineDispatchers(testDispatcher)
    val memoryStore = InMemoryStore(clock)
    
    val skill = MySkill()
    val context = DefaultSkillContext(
        memoryStore = memoryStore,
        dispatchers = dispatchers,
        clock = clock,
        scope = this,
    )
    
    val result = skill.execute(MyInput(...), context)
    
    assertThat(result).isInstanceOf(SkillResult.Success::class.java)
}
```

See `skills/templates/testing/SkillTestTemplate.kt` for comprehensive test patterns.

## 📚 Documentation

### Quick Reference
| Document | Description |
|----------|-------------|
| [AI Context](skills/AI_CONTEXT.md) | Quick reference for AI agents (read first!) |
| [Agent Summary](skills/AGENT_SUMMARY.md) | Core rules, validation, error mapping |
| [Decision Tree](skills/guides/00-decision-tree.md) | Quick navigation for AI agents |
| [Architecture](ARCHITECTURE.md) | Complete architecture documentation |
| [AI Prompts](skills/AI_PROMPTS.md) | Code generation prompts |

### Cheat Sheets
| Document | Description |
|----------|-------------|
| [Validation](VALIDATION_CHEAT_SHEET.md) | Validation functions quick reference |
| [Error Handling](ERROR_HANDLING_CHEAT_SHEET.md) | Error patterns & exception mapping |
| [Testing](TESTING_CHEAT_SHEET.md) | MockK, Turbine, coroutine testing |
| [Compose](COMPOSE_CHEAT_SHEET.md) | Compose patterns & best practices |

### Guides (36 Comprehensive Guides)
| # | Guide | Description |
|---|-------|-------------|
| 00 | [Decision Tree](skills/guides/00-decision-tree.md) | Quick navigation & pattern selection |
| 01 | [Architecture](skills/guides/01-architecture.md) | Clean Architecture patterns |
| 02 | [Coding Conventions](skills/guides/02-coding-conventions.md) | Kotlin coding standards |
| 03 | [Coroutines & Concurrency](skills/guides/03-coroutines-concurrency.md) | Async programming |
| 04 | [Project Structure](skills/guides/04-project-structure.md) | Project organization |
| 05 | [Jetpack Compose](skills/guides/05-jetpack-compose.md) | Compose best practices |
| 06 | [Compose Performance](skills/guides/06-compose-performance.md) | Compose optimization |
| 07 | [State Management](skills/guides/07-state-management.md) | MVI/MVVM patterns |
| 08 | [Dependency Injection](skills/guides/08-dependency-injection.md) | Hilt & Koin setup |
| 09 | [Version Catalog](skills/guides/09-version-catalog.md) | Gradle version catalog |
| 10 | [Error Handling](skills/guides/10-error-handling.md) | Exception patterns |
| 11 | [Testing](skills/guides/11-testing.md) | Unit & UI testing |
| 12 | [Security](skills/guides/12-security.md) | Security best practices |
| 13 | [Navigation](skills/guides/13-navigation.md) | Type-safe navigation |
| 14 | [Offline-First](skills/guides/14-offline-first.md) | Caching & sync |
| 15 | [KMP Readiness](skills/guides/15-kmp-readiness.md) | Kotlin Multiplatform |
| 16 | [Compose Multiplatform](skills/guides/16-compose-multiplatform.md) | CMP patterns |
| 17 | [Build Configuration](skills/guides/17-build-configuration.md) | Build variants |
| 18 | [CI/CD](skills/guides/18-ci-cd.md) | Continuous integration |
| 19 | [Accessibility](skills/guides/19-accessibility.md) | A11y guidelines |
| 20 | [Anti-Patterns](skills/guides/20-anti-patterns.md) | Common mistakes |
| 21 | [Code Review](skills/guides/21-code-review.md) | Review guidelines |
| 22 | [Checklists](skills/guides/22-checklists.md) | Quality checklists |
| 23 | [Memory & Performance](skills/guides/23-memory-performance.md) | Performance optimization |
| 24 | [Validation Rules](skills/guides/24-validation-rules.md) | Validation patterns |
| 25 | [Performance Benchmarks](skills/guides/25-performance-benchmarks.md) | Benchmarking |
| 26 | [Context-Aware Suggestions](skills/guides/26-context-aware-suggestions.md) | AI suggestions |
| 27 | [Refactoring Patterns](skills/guides/27-refactoring-patterns.md) | Refactoring guides |
| 28 | [Code Generation](skills/guides/28-code-generation.md) | Code gen patterns |
| 29 | [Testing Automation](skills/guides/29-testing-automation.md) | Test automation |
| 30 | [Skill Analysis](skills/guides/30-skill-analysis.md) | Skill quality analysis |
| 31 | [Pipeline Patterns](skills/guides/31-pipeline-patterns.md) | Skill pipelines & workflows |
| 32 | [Modern Kotlin Features](skills/guides/32-modern-kotlin-features.md) | Kotlin 2.0+, K2 compiler |
| 33 | [Agent Feedback Loop](skills/guides/33-agent-feedback-loop.md) | Feedback tracking & metrics |
| 34 | [Logging & Analytics](skills/guides/34-logging-analytics.md) | Timber, crash reporting |
| 35 | [Gradle Optimization](skills/guides/35-gradle-optimization.md) | Build optimization |

See [skills/guides/README.md](skills/guides/README.md) for the complete list.

## License

MIT License - Use freely in your projects.
