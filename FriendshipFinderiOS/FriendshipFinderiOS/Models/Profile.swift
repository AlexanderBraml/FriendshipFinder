//
//  Profile.swift
//  FriendshipFinderiOS
//
//  Created by Philipp Mayr on 10.11.23.
//

import Foundation
import SwiftData

@Model
class Profile {
    var name: String
    var bio: String
    var image: Data?
    
    init(name: String = "", bio: String = "", image: Data? = nil) {
        self.name = name
        self.bio = bio
        self.image = image
    }
}
