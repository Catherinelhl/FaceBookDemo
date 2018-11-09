//
//  ViewController.swift
//  FacebookLogin_Sample
//
//  Created by Dong on 2018/11/8.
//  Copyright © 2018 orangeblock.com. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var fbLoginButton: FBSDKLoginButton!
    
    
    private let login = FBSDKLoginManager.init()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
//        let loginButton = FBSDKLoginButton.init()
//        loginButton.center = self.view.center
//        self.view.addSubview(loginButton)
        
        
        fbLoginButton.readPermissions = ["public_profile"]
    }
    
    @IBAction func loginButtonAction(_ sender: UIButton) {
        
        login.logIn(withReadPermissions: ["public_profile"], from: self) { (result, error) in
            if error != nil {
                print(error)
            }else if result?.isCancelled == true {
                print("取消登录")
            }else {
                print("登录成功")
            }
        }
        
    }
    
    @IBAction func logoutButtonAction(_ sender: UIButton) {
        

        login.logOut()
    }
    
}

