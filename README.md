# CoolLib Android Client 

[![Android UI Demo](https://img.shields.io/badge/Android-UI_Demo-3DDC84?style=flat&logo=android&logoColor=white)](https://ryansu.uk/demo/android-demo/)&nbsp;&nbsp;&nbsp;&nbsp;[![Android CI/CD](https://github.com/susui888/coollib-android/actions/workflows/android-ci.yml/badge.svg)](https://github.com/susui888/coollib-android/actions/workflows/android-ci.yml)

Android client for the CoolLib ecosystem. Built with Jetpack Compose and Clean Architecture, designed for offline-first library management with synchronized backend integration.

## Ecosystem

* [CoolLib Server](https://github.com/susui888/CoolLeaf) — Spring Boot backend API
* [CoolLib iOS](https://github.com/susui888/coollib-ios) — SwiftUI client

## Android Metrics
<p><a href="https://ryansu.uk/analytics/mobile/"><img src="https://telemetry-svg.susui888.workers.dev/api/telemetry-android.svg" alt="Android Metrics" width="600" /></a></p>

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

## Data Flow Architecture

```mermaid
flowchart LR

                subgraph DI ["`**Hilt Dependency Injection**`"]
                Hilt{{Hilt\nContainer}}
                end

                subgraph Data ["`**Data Layer**`"]
                direction TB
                Room[(Room\nDatabase)]
                DAO(Data Access\nObjects)
                Impl(RepositoryImpl)
                end

                subgraph Domain ["`**Domain Layer**`"]
                direction TB
                RepoI(interface\nRepository)
                UC(UseCases \n Interactors)
                end

                subgraph Presentation ["`**UI Layer**`"]
                direction TB
                VM(ViewModel)
                UIState([UiState])
                Compose(Jetpack\nCompose)
                end

                Room <--> DAO
                Impl --> DAO

                Impl -.->|Implements| RepoI

                UC --> RepoI
                VM --> UC

                VM -- "Exposes" --> UIState
                UIState -- "Renders" --> Compose
                Compose -- "User Action" --> VM

                Hilt e1@-.->|Inject| DAO
                Hilt e4@-.->|Binds| Impl
                Hilt e2@-.->|Inject| UC
                Hilt e3@-.->|Inject| VM

                e1@{ animate: true }
                e2@{ animate: true }
                e3@{ animate: true }
                e4@{ animate: true }

                %% =========================
                %% STYLE SYSTEM (Light Theme)
                %% =========================
                style Hilt fill:#0284c7,stroke:#0369a1,stroke-width:2px,color:#ffffff
                style Room fill:#334155,stroke:#1e293b,stroke-width:2px,color:#ffffff
                style DAO fill:#475569,stroke:#334155,stroke-width:2px,color:#ffffff
                style Impl fill:#64748b,stroke:#475569,stroke-width:2px,color:#ffffff

                style RepoI fill:#0d9488,stroke:#0f766e,stroke-width:2px,color:#ffffff
                style UC fill:#14b8a6,stroke:#0d9488,stroke-width:2px,color:#ffffff

                style VM fill:#16a34a,stroke:#15803d,stroke-width:2px,color:#ffffff
                style UIState fill:#bbf7d0,stroke:#16a34a,stroke-width:2px,color:#14532d
                style Compose fill:#f0fdf4,stroke:#16a34a,stroke-width:2px,color:#166534

                style DI fill:none,stroke:#0284c7,stroke-dasharray: 5 5
                style Data fill:none,stroke:#475569,stroke-dasharray: 5 5
                style Domain fill:none,stroke:#0d9488,stroke-dasharray: 5 5
                style Presentation fill:none,stroke:#16a34a,stroke-dasharray: 5 5

                linkStyle default stroke:#64748b,stroke-width:1.5px
