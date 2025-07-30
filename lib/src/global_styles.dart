// lib/src/global.dart

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
  static TextStyle get bigTitle => TextStyle(
        fontWeight: FontWeight.bold,
        fontSize: AppSizes.screenWidth * 0.11,
        color: AppColors.primary,
        letterSpacing: 1.5,
      );

  static TextStyle get normalTitle => TextStyle(
        fontWeight: FontWeight.w600,
        fontSize: AppSizes.screenWidth * 0.065,
      );

  static TextStyle get highlightTitle => TextStyle(
      fontWeight: FontWeight.w600,
      fontSize: AppSizes.screenWidth * 0.065,
      color: AppColors.primary,
    );
  
  static TextStyle get whiteTitle => TextStyle(
    fontWeight: FontWeight.w600,
    fontSize: AppSizes.screenWidth * 0.065,
    color: AppColors.white,
  );

  static TextStyle get captiontTitle => TextStyle(
        fontWeight: FontWeight.w500,
        fontSize: AppSizes.screenWidth * 0.035,
        color: Colors.grey.shade700,
    );

  static TextStyle get body => TextStyle(
        fontSize: AppSizes.screenWidth * 0.038,
        color: Colors.black,
      );

  static TextStyle get caption => TextStyle(
        fontSize: AppSizes.screenWidth * 0.035,
        color: Colors.grey.shade600,
      );

  static TextStyle get buttonPrimary => TextStyle(
        fontSize: AppSizes.screenWidth * 0.045,
        fontWeight: FontWeight.w600,
        color: Colors.white,
      );

  static TextStyle get buttonSecondary => TextStyle(
        fontSize: AppSizes.screenWidth * 0.045,
        fontWeight: FontWeight.w600,
        color: AppColors.primary,
      );

  static TextStyle get link => TextStyle(
        fontSize: AppSizes.screenWidth * 0.035,
        fontWeight: FontWeight.w600,
        color: AppColors.primaryLight,
      );

  static TextStyle get hintText => TextStyle(
        fontSize: AppSizes.screenWidth * 0.038,
        color: Colors.grey.shade400,
    );
}