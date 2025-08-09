// lib/src/global_styles.dart

import 'package:flutter/material.dart';

class AppSizes {
  static late double screenWidth;
  static late double screenHeight;
  static late double textScaleFactor;

  static void init(BuildContext context) {
    final size = MediaQuery.of(context).size;
    final mediaQuery = MediaQuery.of(context);

    screenWidth = size.width;
    screenHeight = size.height;
    textScaleFactor = mediaQuery.textScaleFactor;
  }

   static double getResponsiveFontSize(double baseSize, {
    double minScale = 0.85,
    double maxScale = 1.3,
  }) {
    final scaledSize = baseSize * textScaleFactor;
    return scaledSize.clamp(baseSize * minScale, baseSize * maxScale);
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
  static const double _titleLarge = 34.0;
  static const double _titleMedium = 26.0;
  static const double _titleSmall = 22.0;
  static const double _bodyLarge = 20.0;
  static const double _bodyMedium = 18.0;
  static const double _bodySmall = 16.0;
  static const double _caption = 16.0;
  static const double _button = 18.0;
  static const double _link = 16.0;

  static TextStyle get bigTitle => TextStyle(
        fontWeight: FontWeight.bold,
        fontSize: AppSizes.getResponsiveFontSize(_titleLarge),
        color: AppColors.primary,
        letterSpacing: 1.5,
      );

  static TextStyle get normalTitle => TextStyle(
        fontWeight: FontWeight.w600,
        fontSize: AppSizes.getResponsiveFontSize(_titleMedium), 
      );

  static TextStyle get highlightTitle => TextStyle(
        fontWeight: FontWeight.w600,
        fontSize: AppSizes.getResponsiveFontSize(_titleMedium),
        color: AppColors.primary,
      );
  
  static TextStyle get whiteTitle => TextStyle(
        fontWeight: FontWeight.w600,
        fontSize: AppSizes.getResponsiveFontSize(_titleMedium), 
        color: AppColors.white,
      );

  static TextStyle get captiontTitle => TextStyle(
        fontWeight: FontWeight.w500,
        fontSize: AppSizes.getResponsiveFontSize(_bodySmall), 
        color: Colors.grey.shade700,
      );

  static TextStyle get body => TextStyle(
        fontSize: AppSizes.getResponsiveFontSize(_bodyMedium), 
        color: Colors.black,
      );

  static TextStyle get caption => TextStyle(
        fontSize: AppSizes.getResponsiveFontSize(_caption), 
        color: Colors.grey.shade600,
      );

  static TextStyle get buttonPrimary => TextStyle(
        fontSize: AppSizes.getResponsiveFontSize(_button),
        fontWeight: FontWeight.w600,
        color: Colors.white,
      );

  static TextStyle get buttonSecondary => TextStyle(
        fontSize: AppSizes.getResponsiveFontSize(_button),
        fontWeight: FontWeight.w600,
        color: AppColors.primary,
      );

  static TextStyle get link => TextStyle(
        fontSize: AppSizes.getResponsiveFontSize(_link), 
        fontWeight: FontWeight.w600,
        color: AppColors.primaryLight,
      );

  static TextStyle get hintText => TextStyle(
        fontSize: AppSizes.getResponsiveFontSize(_bodyMedium), 
        color: Colors.grey.shade400,
      );

  // Additional utility methods
  static TextStyle getCustomStyle({
    required double fontSize,
    FontWeight? fontWeight,
    Color? color,
    double? letterSpacing,
    double? height,
  }) {
    return TextStyle(
      fontSize: AppSizes.getResponsiveFontSize(fontSize),
      fontWeight: fontWeight,
      color: color,
      letterSpacing: letterSpacing,
      height: height,
    );
  }

  // Method to get font size based on screen size category
  static double getScreenBasedFontSize(double small, double medium, double large) {
    if (AppSizes.screenWidth < 360) return small;
    if (AppSizes.screenWidth < 600) return medium;
    return large;
  }
}

// Extension for quick access to responsive sizes
  extension ResponsiveFontSize on double {
    double get responsive => AppSizes.getResponsiveFontSize(this);
}