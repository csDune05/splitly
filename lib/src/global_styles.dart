// lib/src/global_styles.dart

import 'package:flutter/material.dart';

class AppSizes {
  static late double screenWidth;
  static late double screenHeight;

  static void init(BuildContext context) {
    final size = MediaQuery.of(context).size;
    screenWidth = size.width;
    screenHeight = size.height;
  }
}

class AppColors {
  static const Color primary = Color(0xFF0041C4);
  static const Color primaryLight = Color(0xFF3366FF);
  static const Color primaryDark = Color(0xFF002D99);
  static const Color accent = Color(0xFF5C8DFF);
  static const Color softBlue = Color(0xFFE6EEFF);
  static const Color white = Color(0xFFFFFFFF);
  static const Color grayBlue = Color(0xFFAAB8D4);
  static const Color indigo = Color(0xFF3F51B5);
}

class AppButtonStyles {
  static double normalButtonWidth = AppSizes.screenWidth * 0.8;
  static double normalButtonHeight = AppSizes.screenHeight * 0.02;
}

class AppTextStyles {
  static const double _titleLarge = 32.0;
  static const double _titleMedium = 24.0;
  static const double _titleSmall = 20.0;
  static const double _bodyLarge = 18.0;
  static const double _bodyMedium = 16.0;
  static const double _bodySmall = 14.0;
  static const double _caption = 12.0;
  static const double _button = 16.0;

  static TextStyle get bigTitle => TextStyle(
        fontWeight: FontWeight.bold,
        fontSize: _titleLarge,
        color: AppColors.primary,
        letterSpacing: 1.5,
      );

   static TextStyle get normalTitle => TextStyle(
        fontWeight: FontWeight.w600,
        fontSize: _titleMedium, 
      );

  static TextStyle get highlightTitle => TextStyle(
      fontWeight: FontWeight.w600,
      fontSize: _titleMedium,
      color: AppColors.primary,
    );
  
  static TextStyle get whiteTitle => TextStyle(
    fontWeight: FontWeight.w600,
    fontSize: _titleMedium, 
    color: AppColors.white,
  );

  static TextStyle get captiontTitle => TextStyle(
        fontWeight: FontWeight.w500,
        fontSize: _bodySmall, 
        color: Colors.grey.shade700,
    );

  static TextStyle get body => TextStyle(
        fontSize: _bodyMedium, 
        color: Colors.black,
      );

  static TextStyle get caption => TextStyle(
        fontSize: _caption, 
        color: Colors.grey.shade600,
      );

  static TextStyle get buttonPrimary => TextStyle(
        fontSize: _button,
        fontWeight: FontWeight.w600,
        color: Colors.white,
      );

  static TextStyle get buttonSecondary => TextStyle(
        fontSize: _button,
        fontWeight: FontWeight.w600,
        color: AppColors.primary,
      );

  static TextStyle get link => TextStyle(
        fontSize: _caption, 
        fontWeight: FontWeight.w600,
        color: AppColors.primaryLight,
      );

  static TextStyle get hintText => TextStyle(
        fontSize: _bodyMedium, 
        color: Colors.grey.shade400,
    );
}