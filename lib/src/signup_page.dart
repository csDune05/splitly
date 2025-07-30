import 'package:flutter/material.dart';
import 'global_styles.dart';
import 'screens.dart';

class SignupPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _SignupPageState();
  }
}

class _SignupPageState extends State<SignupPage> {
  final _usernameController = TextEditingController();
  final _passwordController = TextEditingController();
  final _phonenumberController = TextEditingController();
  final _emailController = TextEditingController();
  final _formKey = GlobalKey<FormState>();
  bool _obscurePassword = true;

  @override
  void dispose() {
    _usernameController.dispose();
    _passwordController.dispose();
    _emailController.dispose();
    _phonenumberController.dispose();
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

      appBar: PreferredSize(
        preferredSize: Size.fromHeight(AppSizes.screenHeight * 0.08),

        child: AppBar(
          backgroundColor: Colors.white,
          shadowColor: Colors.grey.withOpacity(0.5), 

          title: Text(
            "Sign up",
            style: AppTextStyles.normalTitle,
          ),

          centerTitle: true,

          elevation: 0.0,

          leading: IconButton(
            icon: Icon(Icons.arrow_back_ios, color: Colors.black),
            onPressed: () => Navigator.pop(context),
          ),
        )
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
                          prefixIcon: Icon(
                            Icons.person,
                            color: Colors.grey.shade500,
                          ),

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

                      SizedBox(height: screenSize.height * 0.025),

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
                          prefixIcon: Icon(
                            Icons.password,
                            color: Colors.grey.shade500,
                          ),

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
                          if (!RegExp(r'[A-Z]').hasMatch(value)) {
                            return 'Password must contain at least one uppercase letter';
                          }
                          if (!RegExp(r'[a-z]').hasMatch(value)) {
                            return 'Password must contain at least one lowercase letter';
                          }
                          if (!RegExp(r'[!@#$%^&*(),.?":{}|<>]').hasMatch(value)) {
                            return 'Password must contain at least one special character';
                          }
                          return null;
                        }
                      ),

                      SizedBox(height: screenSize.height * 0.025),

                      Text(
                        "Email",
                        style: AppTextStyles.captiontTitle, 
                      ),

                      const SizedBox(height: 8),

                      TextFormField(
                        controller: _emailController,

                        decoration: InputDecoration(
                          hintText: 'Enter your email',
                          hintStyle: AppTextStyles.hintText, 
                          filled: true,
                          fillColor: Colors.grey.shade50,
                          prefixIcon: Icon(
                            Icons.email,
                            color: Colors.grey.shade500,
                          ),

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
                            return 'Please enter your email';
                          }
                          if (!RegExp(r'^[\w-\.]+@([\w-]+\.)+[\w]{2,4}$').hasMatch(value)) {
                            return 'Please enter a valid email address';
                          }
                          return null;
                        }

                      ),

                      SizedBox(height: screenSize.height * 0.025),

                      Text(
                        "Phone number",
                        style: AppTextStyles.captiontTitle, 
                      ),

                      const SizedBox(height: 8),

                      TextFormField(
                        controller: _phonenumberController,

                        decoration: InputDecoration(
                          hintText: 'Enter your phone number',
                          hintStyle: AppTextStyles.hintText, 
                          filled: true,
                          fillColor: Colors.grey.shade50,
                          prefixIcon: Icon(
                            Icons.phone,
                            color: Colors.grey.shade500,
                          ),

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
                            return 'Please enter your phone number';
                          }
                          if (!RegExp(r'^\d{10}$').hasMatch(value)) {
                            return 'Phone number must contain exactly 10 digits';
                          }
                          return null;
                        }
                      ),

                      SizedBox(height: screenSize.height * 0.08),

                      SizedBox(
                        width: double.infinity,

                        child: ElevatedButton(
                          onPressed: () {
                            if (_formKey.currentState!.validate()) {
                              ScaffoldMessenger.of(context).showSnackBar(
                                SnackBar(content: Text('Create an account...')),
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
                            'Sign up',
                            style: AppTextStyles.buttonPrimary,
                          ),
                        ),
                      ),

                      SizedBox(height: screenSize.height * 0.02),

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
                )
              )
            )
          )
        )
      )
    );
  }

}