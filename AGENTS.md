# Project Agent Instructions

This is an Android project. When working in this repository, prefer the local
agent skills and project conventions over generic Android advice.

## Local Skills

At the start of a new session, check the local skills under:

```text
.agents/skills/
```

For Android, Kotlin, Jetpack Compose, Clean Architecture, ViewModel, Repository,
UseCase, validation, state management, testing, or code review tasks, read and
apply:

```text
.agents/skills/android-agent-skills/SKILL.md
```

Use the skill's guidance as the source of truth for architecture and code style
unless existing project code clearly establishes a more specific local pattern.

## Working Style

- Read the relevant code before making changes.
- Keep edits scoped to the user's request.
- Follow existing package structure, naming, Gradle setup, and Compose patterns.
- Prefer small, production-ready changes over broad rewrites.
- Do not revert unrelated local changes.

## Android Standards

- Use Kotlin and Jetpack Compose idiomatically.
- Follow Clean Architecture boundaries where applicable:
  ViewModel -> UseCase -> Repository.
- Keep mutable state private and expose immutable state.
- Use one-shot UI events intentionally.
- Preserve coroutine cancellation by rethrowing `CancellationException`.
- Add or update focused tests when behavior changes.

## Verification

When code changes are made, run the most relevant available check before
finishing. Prefer:

```powershell
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'; .\gradlew.bat test
```

For build-sensitive changes, also consider:

```powershell
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'; .\gradlew.bat assembleDebug
```

If a check cannot be run, explain why in the final response.
