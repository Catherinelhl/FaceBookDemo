//
//  ViewController.swift
//  FacebookLogin_Sample
//
//  Created by Dong on 2018/11/8.
//  Copyright © 2018 orangeblock.com. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    /// facebook 第三方登入按钮初始化
    @IBOutlet weak var fbLoginButton: FBSDKLoginButton!
    
    @IBOutlet weak var logTextView: UITextView!
    
    private let login = FBSDKLoginManager.init()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
//        let loginButton = FBSDKLoginButton.init()
//        loginButton.center = self.view.center
//        self.view.addSubview(loginButton)
        
        // 设置获取账户的信息
        fbLoginButton.readPermissions = ["public_profile"]
        fb_getUserInfo()
        
        // 监听Facebook 账户accesstoken变化
        FBSDKProfile.enableUpdates(onAccessTokenChange: true)
        NotificationCenter.default.addObserver(self, selector: #selector(fb_accessTokenDidChange(_:)), name: .FBSDKAccessTokenDidChange, object: nil)
    }
    
    
    @objc private func fb_accessTokenDidChange(_ notification:Notification){
        MyLog(notification.userInfo)
        
        if let didChange = notification.userInfo?[FBSDKAccessTokenDidChangeUserID] as? Int ,
            didChange == 1 {
            if FBSDKAccessToken.current() != nil {
                // 当账户发生改变，且账户存在时，获取账户信息
                fb_getUserInfo()
            }
        }
    }
    
    private func fb_getUserInfo() {
        if let token = FBSDKAccessToken.current() {
            var log = ""
            log += "appID:\(token.appID ?? "nil")\n"
            log += "userID:\(token.userID ?? "nil")\n"
            log += "token:\(token.tokenString ?? "nil")\n"
            FBSDKProfile.loadCurrentProfile { (profile, aError) in
                if aError == nil {
                    if let profile = FBSDKProfile.current() {
                        log += "Profile:\n"
                        log += "firstName:\(profile.firstName ?? "nil")\n"
                        log += "lastName:\(profile.lastName ?? "nil")\n"
                        log += "middleName:\(profile.middleName ?? "nil")\n"
                        log += "name:\(profile.name ?? "nil")\n"
                        log += "linkURL:\(profile.linkURL?.absoluteString ?? "nil")\n"
                        log += "userID:\(profile.userID ?? "nil")\n"
                        self.logTextView.text = log
                    }
                }
            }
            
            self.logTextView.text = log
        }
    }
    
    
    
    @IBAction func loginButtonAction(_ sender: UIButton) {
        
        login.logIn(withReadPermissions: ["public_profile"], from: self) { (result, error) in
            if error != nil {
                MyLog(error)
            }else if result?.isCancelled == true {
                MyLog("取消登录")
            }else {
                MyLog("登录成功")
            }
        }
        
    }
    
    @IBAction func logoutButtonAction(_ sender: UIButton) {
        

        login.logOut()
    }
    
}


// MARK: - 打印方法
func MyLog<T>(_ message : T,file:String = #file,methodName: String = #function, lineNumber: Int = #line){
    #if DEBUG
    let fileName = (file as NSString).lastPathComponent
    let dateForm = DateFormatter.init()
    dateForm.dateFormat = "HH:mm:ss:SSS"
    print("[\(fileName)][\(lineNumber)][\(dateForm.string(from: Date()))]\(methodName):\(message)")
    #endif
    
}

