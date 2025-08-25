import 'package:flutter/material.dart';
import '../src/global_styles.dart';
import '../src/screens.dart';

class HomeNavigationBar extends StatelessWidget {
  final VoidCallback onAddTap;

  HomeNavigationBar({required this.onAddTap});

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 95, 
      decoration: BoxDecoration(
        color: Colors.grey[50],
        border: Border(
          top: BorderSide(
            color: Colors.grey[200]!,
            width: 0.5,
          ),
        ),
        boxShadow: [
          BoxShadow(
            color: Colors.grey.withOpacity(0.3),
            spreadRadius: 0,
            blurRadius: 12,
            offset: Offset(0, -4),
          ),
        ],
      ),
      child: Stack(
        clipBehavior: Clip.none,
        children: [
          Positioned(
            left: 0,
            right: 0,
            top: 8, 
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                _buildNavItem(
                  icon: Icons.home_rounded,
                  isActive: true, 
                  onTap: () {
                    
                  },
                ),

                _buildNavItem(
                  icon: Icons.people_outline_sharp,
                  isActive: false,
                  onTap: () {
                    // TODO Navigate to friends page
                  },
                ),

                _buildAddButton(),

                _buildNavItem(
                  icon: Icons.analytics,
                  isActive: false,
                  onTap: () {
                    // TODO Navigate to stats page
                  },
                ),
                
                _buildNavItem(
                  icon: Icons.account_circle_rounded,
                  isActive: false,
                  onTap: () {
                    // TODO Navigate to profile page
                  },
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildAddButton() {
    return BouncyButton(
      onTap: onAddTap,
      child: Column(
        mainAxisSize: MainAxisSize.min,
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Container(
            width: 50,
            height: 50,
            decoration: BoxDecoration(
              gradient: LinearGradient(
                colors: [
                  AppColors.primary,
                  AppColors.accent,
                ],
                begin: Alignment.topLeft,
                end: Alignment.bottomRight,
              ),
              shape: BoxShape.circle,
              boxShadow: [
                BoxShadow(
                  color: Color(0xFF0084FF).withOpacity(0.4),
                  spreadRadius: 0,
                  blurRadius: 12,
                  offset: Offset(0, 4),
                ),
              ],
            ),
            child: Icon(
              Icons.add_rounded,
              color: Colors.white,
              size: 30,
            ),
          ),
          SizedBox(height: 2),
        ],
      ),
    );
  }

  Widget _buildNavItem({
    required IconData icon,
    required bool isActive,
    required VoidCallback onTap,
  }) {
    return BouncyButton(
      onTap: onTap,
      child: Container(
        padding: EdgeInsets.symmetric(vertical: 10, horizontal: 8),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Container(
              width: 45, 
              height: 45,
              decoration: isActive 
                ? BoxDecoration(
                    color: Colors.white.withOpacity(0.2),
                    shape: BoxShape.circle,
                  )
                : null,
              child: Center(
                child: Icon(
                  icon,
                  color: isActive ? AppColors.primary : Colors.grey[600],
                  size: 30, 
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}