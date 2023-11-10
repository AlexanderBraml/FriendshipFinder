//
//  Imagetransferable.swift
//  FriendshipFinderiOS
//
//  Created by Philipp Mayr on 10.11.23.
//

import SwiftUI

struct ImageTransferable: Transferable {
    let image: UIImage?
    
    static var transferRepresentation: some TransferRepresentation {
        DataRepresentation(importedContentType: .image) { data in
            guard let uiImage = UIImage(data: data) else {
                return ImageTransferable(image: nil)
            }
            return ImageTransferable(image: uiImage)
        }
    }
}
