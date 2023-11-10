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
    
    var body: some View {
        VStack {
            Text("Devices")
            Button {
                if (bc.state == .poweredOn) {
                    bc.startScanning()
                }
            } label: {
                Text("Scan")
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
    BluetoothDeviceListView()
}
