//
//  PeripheralController.swift
//  FriendshipFinderiOS
//
//  Created by Philipp Mayr on 10.11.23.
//

import Foundation
import CoreBluetooth

class PeripheralController: NSObject, ObservableObject, CBPeripheralManagerDelegate {
    static let shared = PeripheralController()
    private var peripheralManager : CBPeripheralManager!
    private var service: CBMutableService!
    
    override private init() {
        super.init()
        peripheralManager = CBPeripheralManager(delegate: self, queue: nil)
        service = CBMutableService(type: CBUUID(nsuuid: UUID(uuidString: "0949F341-11A9-4BF9-BE13-877D2FD8946E")!), primary: true)
    }
    
    func peripheralManagerDidUpdateState(_ peripheral: CBPeripheralManager) {
        print("PC State:")
        switch peripheral.state {
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
                addServices()
            @unknown default:
                print("Unknown State")
            }
    }
    
    func addServices() {
        let valueData = "Friend".data(using: .utf8)
        let myChar = CBMutableCharacteristic(type: CBUUID(nsuuid: UUID(uuidString: "0949F341-11A9-4BF9-BE13-877D2FD8946F")!), properties: [.read], value: valueData, permissions: [.readable])
        service.characteristics = [myChar]
        peripheralManager.add(service)
    }
    
    func startAdvertising() {
        peripheralManager.startAdvertising([CBAdvertisementDataLocalNameKey : "FriendFinder", CBAdvertisementDataServiceUUIDsKey: [service.uuid]])
        print("Started Advertising")
    }
}
