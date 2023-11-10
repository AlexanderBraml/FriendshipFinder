//
//  BluetoothDeviceListView.swift
//  FriendshipFinderiOS
//
//  Created by Philipp Mayr on 10.11.23.
//

import SwiftUI

struct BluetoothDeviceListView: View {
    @ObservedObject var bc: BluetoothController = BluetoothController.shared
    @ObservedObject var pc: PeripheralController = PeripheralController.shared
    
    @Binding var path: NavigationPath
    
    var body: some View {
        VStack {
            Button {
                path.append("profile")
            } label: {
                Text("Profile")
            }
            Text("Devices")
            Button {
                if (bc.state == .poweredOn) {
                    bc.startScanning()
                }
            } label: {
                Text("Scan")
            }
            Button {
                pc.startAdvertising()
            } label: {
                Text("Advertise")
            }
            List {
                ForEach(bc.devices.sorted(by: >)) { device in
                    Text("\(device.name): \(device.id)")
                }
            }
        }
    }
}

#Preview {
    BluetoothDeviceListView(path: .constant(NavigationPath()))
}
