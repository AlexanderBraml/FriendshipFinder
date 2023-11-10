//
//  FriendshipFinderiOSApp.swift
//  FriendshipFinderiOS
//
//  Created by Philipp Mayr on 10.11.23.
//

import SwiftUI

@main
struct FriendshipFinderiOSApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
                .modelContainer(for: Profile.self)
        }
    }
}
