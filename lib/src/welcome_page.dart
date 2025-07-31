import 'package:flutter/material.dart';
import 'screens.dart';
import 'global_styles.dart'; 

class WelcomePage extends StatelessWidget {
  const WelcomePage({super.key});

  @override
  Widget build(BuildContext context) {
    AppSizes.init(context); 

    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: SingleChildScrollView(
          child: Padding(
            padding: EdgeInsets.symmetric(
              horizontal: AppSizes.screenWidth * 0.06,
              vertical: AppSizes.screenHeight * 0.04,
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                SizedBox(height: AppSizes.screenHeight * 0.06),

                Center(
                  child: Column(
                    children: [
                      Image.asset(
                        'assets/images/logo.png',
                        width: AppSizes.screenWidth * 0.45,
                        height: AppSizes.screenHeight * 0.2,
                        fit: BoxFit.contain,
                      ),
                        
                      Text("SPLITLY", style: AppTextStyles.bigTitle),

                      SizedBox(height: AppSizes.screenHeight * 0.01),

                      SizedBox(
                        width: AppSizes.screenWidth * 0.65,
                        child: Text(
                          "Manage your event finances easily and accurately.",
                          style: AppTextStyles.body,
                          textAlign: TextAlign.center,
                        ),
                      ),
                    ],
                  ),
                ),

                SizedBox(height: AppSizes.screenHeight * 0.15),

                Center(
                  child: Column(
                    children: [
                      SizedBox(
                        width: 500,

                        child: ElevatedButton(
                          onPressed: () {
                           
                          },
                          style: ElevatedButton.styleFrom(
                            backgroundColor: const Color.fromARGB(255, 232, 240, 255),
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(10),
                            ),
                            padding: EdgeInsets.symmetric(
                              vertical: 20,
                            ),
                          ),
                          child: Text(
                            'Sign in with Google',
                            style: AppTextStyles.buttonSecondary
                          ),
                        ),
                      ),

                      SizedBox(height: AppSizes.screenHeight * 0.025),

                      SizedBox(
                        width: double.infinity,

                        child: ElevatedButton(
                          onPressed: () {
                            Navigator.push(
                                context,
                                MaterialPageRoute(builder: (context) => SignupPage()),
                            );
                          },
                          style: ElevatedButton.styleFrom(
                            backgroundColor: const Color(0xFF0041C4),
                            overlayColor: Colors.black,
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(10),
                            ),
                            padding: EdgeInsets.symmetric(
                              vertical: 20,
                            ),
                          ),
                          child: Text(
                            'Create an account',
                            style: AppTextStyles.buttonPrimary
                          ),
                        ),
                      ),
                    ],
                  ),
                ),

                SizedBox(height: AppSizes.screenHeight * 0.02),

                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      "Already have an account? ",
                      style: AppTextStyles.caption,
                    ),
                    InkWell(
                      onTap: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(builder: (context) => LoginPage()),
                        );
                      },
                      splashColor: Colors.blue.withOpacity(0.2),
                      borderRadius: BorderRadius.circular(4),
                      child: Padding(
                        padding: const EdgeInsets.all(4.0),
                        child: Text(
                          'Sign in',
                          style: AppTextStyles.link,
                        ),
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
        )
      ),
    );
  }
}