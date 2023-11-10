//
//  EditableTextField.swift
//  FriendshipFinderiOS
//
//  Created by Philipp Mayr on 10.11.23.
//

import SwiftUI

struct EditableTextArea: View {
    @Environment(\.editMode) private var editMode

    let label: String
    @Binding var text: String
    
    init(_ label: String, text: Binding<String>) {
        self.label = label
        self._text = text
    }
    
    var body: some View {
        if editMode?.wrappedValue.isEditing == true {
            TextEditor(text: $text)
        } else {
            Text(text == "" ? label : text)
        }
    }
}

#Preview {
    EditableTextArea("Preview", text: .constant("Cool"))
}
