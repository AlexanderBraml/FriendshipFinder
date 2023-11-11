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
        let test = [CBUUID(nsuuid: UUID(uuidString: "0949F341-11A9-4BF9-BE13-877D2FD8946E")!)]
        centralManager.scanForPeripherals(withServices: test, options: nil)
    }
    
    func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral,
                        advertisementData: [String: Any], rssi: NSNumber) {
        let (success, _) = nearbyDevices.insert(Device(id: peripheral.identifier, name: peripheral.name ?? "Unknown", peripheral: peripheral))
        if (success) {
            peripheral.delegate = self
            central.connect(peripheral)
        }
    }
    
    func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral) {
        peripheral.discoverServices(nil)
    }
    
    func centralManager(
        _ central: CBCentralManager,
        didFailToConnect peripheral: CBPeripheral,
        error: Error?
    ) {
        print(error)
    }
    
    func peripheral(_ peripheral: CBPeripheral, didDiscoverServices error: Error?) {
        print(peripheral.name)
        print(peripheral.services)
        for service in peripheral.services ?? [] {
            let uuid = CBUUID(nsuuid: UUID(uuidString: "0949F341-11A9-4BF9-BE13-877D2FD8946F")!)
            peripheral.discoverCharacteristics([uuid], for: service)
        }
    }
    
    func peripheral(_ peripheral: CBPeripheral, didModifyServices invalidatedServices: [CBService]) {
        print("ModifyServices")
    }
    
    func peripheral(_ peripheral: CBPeripheral, didDiscoverCharacteristicsFor service: CBService, error: Error?) {
        print(service.characteristics)
        for char in service.characteristics ?? [] {
            print("reading \(char.uuid)")
            let characteristic = char
            if characteristic.properties.contains(.broadcast) {
                print("\tbroadcast")
            }
            if characteristic.properties.contains(.read) {
                print("\tread")
                peripheral.readValue(for: char)
            }
            if characteristic.properties.contains(.writeWithoutResponse) {
                print("\twrite without response")
            }
            if characteristic.properties.contains(.write) {
                print("\twrite")
            }
            if characteristic.properties.contains(.notify) {
                print("\tnotify")
            }
            if characteristic.properties.contains(.indicate) {
                print("\tindicate")
            }
            if characteristic.properties.contains(.authenticatedSignedWrites) {
                print("\tauthenticated signed writes")
            }
            if characteristic.properties.contains(.extendedProperties) {
                print("\textended properties")
            }
            if characteristic.properties.contains(.notifyEncryptionRequired) {
                print("\tnotify encryption required")
            }
            if characteristic.properties.contains(.indicateEncryptionRequired) {
                print("\tindicate encryption required")
            }
        }
    }
    
    func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor characteristic: CBCharacteristic, error: Error?) {
        if let data = characteristic.value {
            print(String(data: data, encoding: .utf8))
        } else {
            print("no data")
        }
    }
}

struct Device: Identifiable, Hashable, Comparable {
    let id: UUID
    let name: String
    let peripheral: CBPeripheral
    
    static func == (lhs: Device, rhs: Device) -> Bool {
        lhs.id == rhs.id
    }
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }
    
    static func < (lhs: Device, rhs: Device) -> Bool {
        lhs.id < rhs.id
    }
}
