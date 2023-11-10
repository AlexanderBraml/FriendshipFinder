//
//  BluetoothController.swift
//  FriendshipFinderiOS
//
//  Created by Philipp Mayr on 10.11.23.
//

import Foundation
import SwiftUI
import CoreBluetooth

class BluetoothController: NSObject, ObservableObject, CBCentralManagerDelegate, CBPeripheralDelegate {
    static let shared = BluetoothController()
    private var centralManager: CBCentralManager!
    private var nearbyDevices: Set<Device> = []
    @Published var devices: Set<Device> = []

    override private init() {
        super.init()
        centralManager = CBCentralManager(delegate: self, queue: nil)
    }
    
    var state: CBManagerState {
        centralManager.state
    }

    func centralManagerDidUpdateState(_ central: CBCentralManager) {
        print("BC State:")
        switch (central.state) {
        case .unknown:
            print("Bluetooth Device is UNKNOWN")
        case .unsupported:
            print("Bluetooth Device is UNSUPPORTED")
        case .unauthorized:
            print("Bluetooth Device is UNAUTHORIZED")
        case .resetting:
            print("Bluetooth Device is RESETTING")
        case .poweredOff:
            print("Bluetooth Device is POWERED OFF")
        case .poweredOn:
            print("Bluetooth Device is POWERED ON")
        @unknown default:
            print("Unknown State")
        }
    }

    func startScanning() {
        centralManager.scanForPeripherals(withServices: nil, options: nil)
    }

    func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral,
                        advertisementData: [String: Any], rssi: NSNumber) {
        let (success, _) = nearbyDevices.insert(Device(id: peripheral.identifier, name: peripheral.name ?? "Unknown", peripheral: peripheral))
        if (success) {
            central.connect(peripheral)
            peripheral.delegate = self
        }
    }
    
    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral) {
        peripheral.discoverServices(nil)
    }
    
    func peripheral(_ peripheral: CBPeripheral, didDiscoverServices error: Error?) {
        print(peripheral.name)
        print(peripheral.services)
    }
    
    func peripheral(_ peripheral: CBPeripheral, didModifyServices invalidatedServices: [CBService]) {
        print("ModifyServices")
    }
}

struct Device: Identifiable, Hashable, Comparable {
    let id: UUID
    let name: String
    let peripheral: CBPeripheral
    
    static func == (lhs: Device, rhs: Device) -> Bool {
        lhs.id == rhs.id
    }
    
    static func < (lhs: Device, rhs: Device) -> Bool {
        lhs.id < rhs.id
    }
}
