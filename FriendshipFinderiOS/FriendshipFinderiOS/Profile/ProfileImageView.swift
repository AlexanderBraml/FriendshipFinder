//
//  ProfileImageView.swift
//  FriendshipFinderiOS
//
//  Created by Philipp Mayr on 10.11.23.
//

import SwiftUI
import PhotosUI

struct ProfileImageView: View {
    @Environment(\.editMode) private var editMode
    
    @Binding var image: UIImage?
    @State var imageItem: PhotosPickerItem? = nil
    
    var body: some View {
        if editMode?.wrappedValue.isEditing == true {
            PhotosPicker(selection: $imageItem,
                         matching: .images,
                         photoLibrary: .shared()) {
                Group {
                    if let image = image {
                        Image(uiImage: image)
                            .resizable()
                            .scaledToFit()
                    } else {
                        Image(systemName: "person.crop.circle")
                            .resizable()
                    }
                }
            }
            .onChange(of: imageItem) { oldValue, newValue in
                newValue?.loadTransferable(type: ImageTransferable.self) { result in
                    DispatchQueue.main.async {
                        guard newValue != nil else {
                            print("Failed to get the selected item.")
                            return
                        }
                        switch result {
                        case .success(let profileImage?):
                            image = profileImage.image
                        case .success(nil):
                            print("empty")
                        case .failure(let error):
                            print("error: \(error)")
                        }
                    }
                }
            }
        } else {
            if let image = image {
                Image(uiImage: image)
                    .resizable()
                    .scaledToFit()
            } else {
                Image(systemName: "person.crop.circle")
                    .resizable()
            }
        }
    }
}

#Preview {
    ProfileImageView(image: .constant(UIImage(systemName: "person.crop.circle")!))
}
