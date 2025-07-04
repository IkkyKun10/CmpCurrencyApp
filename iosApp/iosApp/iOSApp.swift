import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

     init() {
         AppModuleKt.initializeKoin()
     }

    var body: some Scene {
        WindowGroup {
            ContentView().ignoresSafeArea() // Compose has own keyboard handler
        }
    }
}
