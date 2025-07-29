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

class AppButtonStyles {
  static double normalButtonWidth = AppSizes.screenWidth * 0.8;
  static double normalButtonHeight = AppSizes.screenHeight * 0.02;

  static Color primaryButtonColor = Color(0xFF0041C4);
}

class AppTextStyles {
  static TextStyle get bigTitle => TextStyle(
        fontWeight: FontWeight.bold,
        fontSize: AppSizes.screenWidth * 0.11,
        color: const Color(0xFF0041C4),
        letterSpacing: 1.5,
      );

  static TextStyle get normalTitle => TextStyle(
        fontWeight: FontWeight.w600,
        fontSize: AppSizes.screenWidth * 0.065,
      );

  static TextStyle get highlightTitle => TextStyle(
      fontWeight: FontWeight.w600,
      fontSize: AppSizes.screenWidth * 0.065,
      color: const Color(0xFF0041C4),
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
        color: const Color(0xFF0041C4),
      );

  static TextStyle get link => TextStyle(
        fontSize: AppSizes.screenWidth * 0.035,
        fontWeight: FontWeight.w600,
        color: const Color(0xFF0041C4),
      );

  static TextStyle get hintText => TextStyle(
        fontSize: AppSizes.screenWidth * 0.038,
        color: Colors.grey.shade400,
    );
}