import 'package:flutter/material.dart';
import '../src/global_styles.dart';
import '../src/screens.dart';

class HomeNavigationBar extends StatelessWidget {
  final VoidCallback onAddTap;

  HomeNavigationBar({required this.onAddTap});

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 120, 
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
            color: Colors.grey.withOpacity(0.15),
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
            top: 15, 
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                _buildNavItem(
                  icon: Icons.home_rounded,
                  label: 'Home',
                  isActive: true, 
                  onTap: () {
                    
                  },
                ),

                _buildNavItem(
                  icon: Icons.people_rounded,
                  label: 'Groups',
                  isActive: false,
                  onTap: () {
                    // TODO Navigate to groups page
                  },
                ),

                _buildAddButton(),

                _buildNavItem(
                  icon: Icons.person_add_rounded,
                  label: 'Friends',
                  isActive: false,
                  onTap: () {
                    // TODO Navigate to friends page
                  },
                ),
                
                _buildNavItem(
                  icon: Icons.account_circle_rounded,
                  label: 'Profile',
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
            width: 54,
            height: 54,
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
              size: 28,
            ),
          ),
          SizedBox(height: 2),
        ],
      ),
    );
  }

  Widget _buildNavItem({
    required IconData icon,
    required String label,
    required bool isActive,
    required VoidCallback onTap,
  }) {
    return BouncyButton(
      onTap: onTap,
      child: Container(
        padding: EdgeInsets.symmetric(vertical: 4, horizontal: 8),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Container(
              width: 48, 
              height: 48,
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
                  size: 28, 
                ),
              ),
            ),
            Text(
              label,
              style: TextStyle(
                color: isActive ? AppColors.primary : Colors.grey[600],
                fontSize: 14,
                fontWeight: isActive ? FontWeight.w600 : FontWeight.w400,
              ),
            ),
          ],
        ),
      ),
    );
  }
}