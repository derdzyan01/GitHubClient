# GitHubClient

An Android client for GitHub. Sign in with your GitHub account, browse your repositories, explore other users and their repos, and personalise your profile and theme.

Built as a multi-module app following Clean Architecture, with each feature developed on its own branch and merged through pull requests.

## Screenshots

| Login | My repositories | Users |
| :---: | :---: | :---: |
| <img width="1080" height="2400" alt="login" src="https://github.com/user-attachments/assets/16b11493-ea31-4d0d-8781-12e18adcca23" /> |<img width="1080" height="2400" alt="home" src="https://github.com/user-attachments/assets/7719b066-e850-4b47-9bf3-f652dbf3d4b2" /> | <img width="1080" height="2400" alt="users" src="https://github.com/user-attachments/assets/8614875e-49e0-4de6-899a-f96e3fd1f0ec" /> |

| User details | Profile | Settings |
| :---: | :---: | :---: |
| <img width="1080" height="2400" alt="user_details" src="https://github.com/user-attachments/assets/00daf071-4b28-4b1f-8651-ff5032b6ed0f" /> | <img width="1080" height="2400" alt="profile" src="https://github.com/user-attachments/assets/717b2da2-5f0e-4793-803c-784c55bdb473" /> | <img width="1080" height="2400" alt="theme" src="https://github.com/user-attachments/assets/56079c7c-ed7b-495a-ae57-99ab33268260" /> |



## Features

- **Authentication**: sign in with a GitHub username and personal access token; the session is remembered between launches.
- **My repositories**: paginated list of your repositories with infinite scroll and pull-to-refresh, cached locally in Room.
- **Users**: browse GitHub users with paging and pull-to-refresh.
- **User details**: open any user to see their public repositories.
- **Profile**: your GitHub profile, with a custom avatar picked from the gallery (Photo Picker) and logout.
- **Settings**: light, dark or system theme.

## Architecture

```
app             → entry point, navigation host, Hilt setup
feature_auth    → login screen (presentation)
feature_main    → home, users, user details, profile, settings (presentation)
domain          → models, repository interfaces, use cases (pure Kotlin)
data            → Retrofit API, Room database, DataStore, repository implementations, DI modules
```

- **Presentation**: MVVM. Fragments observe ViewModels; UI state is exposed with StateFlow / LiveData.
- **Domain**: one use case per action (e.g. `LoginUseCase`, `GetReposUseCase`, `GetUserReposUseCase`).
- **Data**: repositories combine the GitHub REST API with a Room cache; DTOs and entities are mapped to domain models.
- Feature modules depend only on `domain`, never on each other.

## Tech stack

| Area | Libraries |
| --- | --- |
| Language | Kotlin |
| UI | XML layouts, Fragments, Material Components, Navigation Component, SwipeRefreshLayout, SplashScreen API |
| Architecture | MVVM, Clean Architecture, multi-module Gradle project |
| Dependency injection | Hilt |
| Networking | Retrofit, OkHttp (auth + logging interceptors), Gson |
| Async | RxJava 3, Kotlin Coroutines & Flow |
| Storage | Room, DataStore Preferences |
| Images | Coil |

## Getting started

1. Clone the repository and open it in Android Studio.
2. Create a [personal access token](https://github.com/settings/tokens) on GitHub with `repo` and `read:user` scopes.
3. Run the app on a device or emulator (min SDK 24) and sign in with your username and the token.

## What I'd improve next

- Migrate the UI to Jetpack Compose.
- Unify async code on Coroutines/Flow instead of mixing with RxJava.
- Store credentials encrypted (OAuth flow or an encrypted token store).
- Add unit tests for use cases and ViewModels, plus a GitHub Actions build.
