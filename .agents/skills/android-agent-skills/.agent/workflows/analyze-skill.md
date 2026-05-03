---
description: Analyze Agent Skill quality and generate improvements
---

# Analyze Skill Workflow

Use this workflow to analyze and improve Agent Skill quality.

## Prerequisites
- [ ] Skill file is accessible
- [ ] Read `skills/guides/30-skill-analysis.md` for quality criteria

## Workflow Steps

### Step 1: Identify Skill
// turbo
```
Locate the skill file:
- Check if extends BaseSkill<I, O>
- Identify Input and Output types
- List dependencies
```

### Step 2: Analyze Validation Coverage
// turbo
```
Check validate() method:
- List ALL input fields
- Check each field has validation
- Use @analyze-skill prompt for scoring
```

**Validation Function Reference:**
| Field Type | Required Function |
|------------|-------------------|
| String | `requireNotBlank` |
| Email | `requireValidEmail` |
| URL | `requireValidUrl` |
| Phone | `requireValidPhone` |
| Number range | `requireInRange` |
| Positive number | `requirePositive` |
| Collection | `requireNotEmpty` or `requireSize` |
| Confidence (0-1) | `requireValidConfidence` |

### Step 3: Analyze Error Handling
// turbo
```
Check doExecute() method:
- Has try-catch block?
- Rethrows CancellationException?
- Maps specific error types?
- Returns SkillResult (never throws)?
```

**Error Pattern Check:**
```kotlin
// ✅ CORRECT
try {
    // work
} catch (e: CancellationException) {
    throw e  // MUST rethrow
} catch (e: IOException) {
    TypedSkillError.Network(...).toFailure()
} catch (e: Exception) {
    TypedSkillError.Execution(...).toFailure()
}

// ❌ WRONG - swallows cancellation
catch (e: Exception) {
    SkillResult.failure(...)  // BAD!
}
```

### Step 4: Analyze Cancellation Support
// turbo
```
Check for:
- checkCancellation() at start of doExecute()
- checkCancellation() in loops
- withContext(NonCancellable) for cleanup
```

### Step 5: Analyze Documentation
// turbo
```
Check KDoc:
- Class description?
- @param for all parameters?
- @return documentation?
- Usage example in KDoc?
- @see references?
```

### Step 6: Calculate Quality Score
Use: `@analyze-skill [SkillName]`

**Scoring:**
| Dimension | Max Points |
|-----------|------------|
| Validation Coverage | 25 |
| Error Handling | 25 |
| Cancellation Support | 20 |
| Documentation | 15 |
| Performance | 10 |
| Testability | 5 |
| **Total** | **100** |

### Step 7: Generate Improvements
If score < 90, generate fixes:
- Use `@add-validation` for missing validations
- Fix error handling pattern
- Add checkCancellation() calls
- Complete KDoc

### Step 8: Generate Tests
Use: `@gen-skill-test [SkillName]`

## Quality Targets
- [ ] Score ≥ 90/100
- [ ] 100% validation coverage
- [ ] CancellationException rethrown
- [ ] All fields documented
- [ ] Tests for all scenarios
