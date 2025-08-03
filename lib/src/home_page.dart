import 'package:flutter/material.dart';
import 'screens.dart';
import 'global_styles.dart';

class HomePage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _HomePageState();
  }
}

class _HomePageState extends State<HomePage> {
  @override
  Widget build(BuildContext context) {
    AppSizes.init(context);

    // Get screen size for responsive design
    final screenSize = MediaQuery.of(context).size;
    final isSmallScreen = screenSize.width < 600;
    final horizontalPadding = isSmallScreen ? 24.0 : screenSize.width * 0.1;
    final maxWidth = isSmallScreen ? double.infinity : 400.0;

    return SafeArea(
      child: Scaffold(
        appBar: AppBar(

        ),

        body: SingleChildScrollView(
          child: SafeArea(
            child: Padding(
              padding: EdgeInsets.symmetric(horizontal: horizontalPadding),
              child: Center(
                child: ConstrainedBox(
                  constraints: BoxConstraints(maxWidth: maxWidth),
                ),
              ),
            ),
          ),
        ),

        bottomNavigationBar: NavigationBar(
          destinations: [

          ],
        ),
      ),
    );
  }
}