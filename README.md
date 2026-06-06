# CoolLib Android Client &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[![Android UI Demo](https://img.shields.io/badge/Android-UI_Demo-3DDC84?style=flat&logo=android&logoColor=white)](https://ryansu.uk/android-demo/)&nbsp;&nbsp;&nbsp;&nbsp;[![Android CI/CD](https://github.com/susui888/coollib-android/actions/workflows/android-ci.yml/badge.svg)](https://github.com/susui888/coollib-android/actions/workflows/android-ci.yml)

<p>
  <img src="https://img.shields.io/badge/Kotlin-2.x-7F52FF"/>&nbsp;
  <img src="https://img.shields.io/badge/Jetpack_Compose-UI-3DDC84"/>&nbsp;
  <img src="https://img.shields.io/badge/Material_3-Design-6750A4"/>&nbsp;
  <img src="https://img.shields.io/badge/Architecture-Clean_Architecture-4B5563?labelColor=111827"/>
</p>

<p>
  <img src="https://img.shields.io/badge/Room-Local_DB-4F5B66"/>&nbsp;
  <img src="https://img.shields.io/badge/Retrofit-Networking-048A81"/>&nbsp;
  <img src="https://img.shields.io/badge/Hilt-DI-7FADF2"/>&nbsp;
  <img src="https://img.shields.io/badge/CameraX-Barcode_Scan-orange"/>&nbsp;
</p>

Android client for the CoolLib ecosystem. Built with Jetpack Compose and Clean Architecture, designed for offline-first library management with synchronized backend integration.

## Ecosystem

* [CoolLib Server](https://github.com/susui888/CoolLeaf) — Spring Boot backend API
* [CoolLib iOS](https://github.com/susui888/coollib-ios) — SwiftUI client

## Tech Stack

### Core

* Kotlin
* Jetpack Compose
* Material 3

### Architecture

* Clean Architecture
* MVVM
* Hilt (Dependency Injection)

### Data Layer

* Room (local persistence)
* Retrofit + OkHttp (network layer)
* JWT-based authentication sync

### Device Integration

* CameraX (barcode scanning)
* ML Kit (barcode recognition)
* AndroidX lifecycle components

## Capabilities

* Offline-first library management with local persistence
* ISBN scanning for fast book ingestion
* JWT-secured synchronization with backend API
* Reactive UI built with Jetpack Compose
* Modular architecture aligned with server-side Clean Architecture design
* Efficient DTO-based synchronization with backend services

## Architecture Overview

```mermaid
flowchart LR
    UI(Compose UI) --> VM(ViewModel)
    VM --> UseCase(Use Cases)
    UseCase --> Repo(Repository)
    Repo --> Local[(Room DB)]
    Repo --> Remote(Retrofit API)

    style UI fill:#22c55e,stroke:#16a34a,stroke-width:2px,color:#fff
    style VM fill:#475569,stroke:#334155,stroke-width:2px,color:#fff
    style UseCase fill:#475569,stroke:#334155,stroke-width:2px,color:#fff
    style Repo fill:#475569,stroke:#334155,stroke-width:2px,color:#fff
    style Local fill:#bfdbfe,stroke:#60a5fa,stroke-width:1px,color:#1f2937
    style Remote fill:#3b82f6,stroke:#2563eb,stroke-width:2px,color:#fff
    linkStyle default stroke:#94a3b8,stroke-width:1.5px
