//
//  ProfileView.swift
//  FriendshipFinderiOS
//
//  Created by Philipp Mayr on 10.11.23.
//

import SwiftUI
import SwiftData

struct ProfileView: View {
    @Environment(\.editMode) private var editMode
    @Environment(\.modelContext) var modelContext
    
    @Query var profiles: [Profile]
    @State var profile: Profile?
    
    var body: some View {
        Group {
            if let profile {
                VStack {
                    ProfileImageView(image: .init(get: {
                        guard let data = profile.image else { return nil }
                        guard let image = UIImage(data: data) else { return nil }
                        return image
                    }, set: { image in
                        profile.image = image?.pngData()
                    }))
                    .clipShape(Circle())
                    .frame(width: 200, height: 200)
                    List {
                        EditableText("Name", text: .init(get: { profile.name }, set: { name in
                            profile.name = name
                        }))
                        EditableTextArea("Bio", text: .init(get: {
                            profile.bio
                        }, set: { bio in
                            profile.bio = bio
                        }))
                    }
                }
                .toolbar {
                    EditButton()
                }
            } else {
                ProgressView()
            }
        }
        .onAppear {
            if profiles.isEmpty {
                let newProfile = Profile()
                modelContext.insert(newProfile)
                profile = newProfile
            } else {
                profile = profiles.first
            }
        }
        .onChange(of: editMode?.wrappedValue) { oldValue, newValue in
            if let newValue {
                if newValue.isEditing == false {
                    try! modelContext.save()
                }
            }
        }
    }
}

#Preview {
    NavigationView {
        ProfileView()
    }
}
