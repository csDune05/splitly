import 'package:flutter/material.dart';
import 'screens.dart';
import 'global_styles.dart'; 

class LoginPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _LoginPageState();
  }
}

class _LoginPageState extends State<LoginPage> {
  final _usernameController = TextEditingController();
  final _passwordController = TextEditingController();
  final _formKey = GlobalKey<FormState>();
  bool _obscurePassword = true;

  @override
  void dispose() {
    _usernameController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    AppSizes.init(context);
    
    // Get screen size for responsive design
    final screenSize = MediaQuery.of(context).size;
    final isSmallScreen = screenSize.width < 600;
    final horizontalPadding = isSmallScreen ? 24.0 : screenSize.width * 0.1;
    final maxWidth = isSmallScreen ? double.infinity : 400.0;

    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        backgroundColor: Colors.white,

        title: Text(
          "Login",
          style: AppTextStyles.normalTitle,
        ),

        elevation: 0,

        leading: IconButton(
          icon: Icon(Icons.arrow_back_ios, color: Colors.black),
          onPressed: () => Navigator.pop(context),
        ),
      ),
      
      body: SafeArea(
        child: SingleChildScrollView(
          child: Padding(
            padding: EdgeInsets.symmetric(horizontal: horizontalPadding),
            child: Center(
              child: ConstrainedBox(
                constraints: BoxConstraints(maxWidth: maxWidth),
                child: Form(
                  key: _formKey,

                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      SizedBox(height: AppSizes.screenHeight * 0.03),
                      
                      Text(
                        "Welcome Back",
                        style: AppTextStyles.highlightTitle,
                      ),

                      SizedBox(height: screenSize.height * 0.015),

                      Text(
                        "Hello there, sign in to continue!",
                        style: AppTextStyles.body,
                      ),

                      SizedBox(height: screenSize.height * 0.05),

                      Text(
                        "Username",
                        style: AppTextStyles.captiontTitle, 
                      ),

                      const SizedBox(height: 8),

                      TextFormField(
                        controller: _usernameController,

                        decoration: InputDecoration(
                          hintText: 'Enter your username',
                          hintStyle: AppTextStyles.hintText, 
                          filled: true,
                          fillColor: Colors.grey.shade50,

                          border: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(12),
                            borderSide: BorderSide(color: Colors.grey.shade200),
                          ),

                          enabledBorder: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(12),
                            borderSide: BorderSide(color: Colors.grey.shade200),
                          ),

                          focusedBorder: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(12),
                            borderSide: BorderSide(color: AppColors.primaryLight, width: 2),
                          ),

                          contentPadding: EdgeInsets.symmetric(
                            horizontal: 16, 
                            vertical: isSmallScreen ? 16 : 18,
                          ),
                        ),

                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return 'Please enter your username';
                          }
                          return null;
                        },
                      ),

                      SizedBox(height: screenSize.height * 0.04),

                      Text(
                        "Password",
                        style: AppTextStyles.captiontTitle,
                      ),

                      const SizedBox(height: 8),

                      TextFormField(
                        controller: _passwordController,
                        obscureText: _obscurePassword,

                        decoration: InputDecoration(
                          hintText: 'Enter your password',
                          hintStyle: AppTextStyles.hintText, 
                          filled: true,
                          fillColor: Colors.grey.shade50,

                          border: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(12),
                            borderSide: BorderSide(color: Colors.grey.shade200),
                          ),

                          enabledBorder: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(12),
                            borderSide: BorderSide(color: Colors.grey.shade200),
                          ),

                          focusedBorder: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(12),
                            borderSide: BorderSide(color: AppColors.primaryLight, width: 2),
                          ),

                          contentPadding: EdgeInsets.symmetric(
                            horizontal: 16, 
                            vertical: isSmallScreen ? 16 : 18,
                          ),

                          suffixIcon: IconButton(
                            icon: Icon(
                              _obscurePassword ? Icons.visibility_off : Icons.visibility,
                              color: Colors.grey.shade500,
                            ),

                            onPressed: () {
                              setState(() {
                                _obscurePassword = !_obscurePassword;
                              });
                            },
                          ),
                        ),

                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return 'Please enter your password';
                          }
                          if (value.length < 6) {
                            return 'Password must be at least 6 characters';
                          }
                          return null;
                        },
                      ),

                      SizedBox(height: screenSize.height * 0.02),

                      Align(
                        alignment: Alignment.centerLeft,
                        child: GestureDetector(
                          onTap: () {
                            // Navigate to forgot password screen
                          },
                          child: Text(
                            'Forgot Password?',
                            style: AppTextStyles.link,
                          ),
                        ),
                      ),

                      SizedBox(height: screenSize.height * 0.08),

                      SizedBox(
                        width: double.infinity,

                        child: ElevatedButton(
                          onPressed: () {
                            if (_formKey.currentState!.validate()) {
                              ScaffoldMessenger.of(context).showSnackBar(
                                SnackBar(content: Text('Signing in...')),
                              );
                            }
                          },

                          style: ElevatedButton.styleFrom(
                            backgroundColor: AppColors.primary,

                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(12),
                            ),
                            padding: EdgeInsets.symmetric(
                              vertical: isSmallScreen ? 16 : 18,
                            ),
                            elevation: 0,
                          ),

                          child: Text(
                            'Sign in',
                            style: AppTextStyles.buttonPrimary,
                          ),
                        ),
                      ),

                      SizedBox(height: screenSize.height * 0.02),

                      Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Text(
                            "Don't have an account? ",
                            style: AppTextStyles.caption,
                          ),
                          InkWell(
                            onTap: () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(builder: (context) => SignupPage()),
                              );
                            },
                            splashColor: Colors.blue.withOpacity(0.2),
                            borderRadius: BorderRadius.circular(4),
                            child: Padding(
                              padding: const EdgeInsets.all(4.0),
                              child: Text(
                                'Sign up',
                                style: AppTextStyles.link,
                              ),
                            ),
                          ),
                        ],
                      ),

                      SizedBox(height: screenSize.height * 0.03),
                    ],
                  ),
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}

