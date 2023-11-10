//
//  ContentView.swift
//  FriendshipFinderiOS
//
//  Created by Philipp Mayr on 10.11.23.
//

import SwiftUI
import SwiftData

struct ContentView: View {
    @Query var profiles: [Profile]
    
    @State var path = NavigationPath()
    
    var body: some View {
        NavigationStack(path: $path) {
            BluetoothDeviceListView(path: $path)
                .navigationDestination(for: String.self) { string in
                    switch (string) {
                    case "profile":
                        ProfileView()
                    default:
                        Text(string)
                    }
                }
        }
    }
}

#Preview {
    ContentView()
}
