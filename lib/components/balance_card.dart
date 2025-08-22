import 'package:flutter/material.dart';
import '../src/global_styles.dart';

class BalanceCard extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.symmetric(horizontal: 16),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(20),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.1),
            blurRadius: 20,
            offset: Offset(0, 8),
          )
        ],
      ),
      child: Padding(
        padding: const EdgeInsets.all(20.0), 
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  'YOUR BALANCE',
                  style: TextStyle(
                    color: Colors.grey[600],
                    fontSize: 12,
                    fontWeight: FontWeight.w500,
                    letterSpacing: 0.5,
                  ),
                ),
                Icon(
                  Icons.visibility_outlined,
                  color: Colors.grey[400],
                  size: 20,
                ),
              ],
            ),
            SizedBox(height: 4), 
            Row(
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                Text(
                  '\$41,379.00',
                  style: TextStyle(
                    color: Colors.black,
                    fontSize: 28,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                SizedBox(width: 8),
                Container(
                  padding: EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                  decoration: BoxDecoration(
                    color: Colors.green.withOpacity(0.1),
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: Icon(
                    Icons.trending_up,
                    color: Colors.green,
                    size: 16,
                  ),
                ),
              ],
            ),
            SizedBox(height: 18), 
            Row(
              children: [
                _buildActionButton(
                  icon: Icons.arrow_upward,
                  label: 'Transfer',
                  color: AppColors.primary,
                ),
                SizedBox(width: 12),
                _buildActionButton(
                  icon: Icons.arrow_downward,
                  label: 'Withdraw',
                  color: Colors.purple,
                ),
                SizedBox(width: 12),
                _buildActionButton(
                  icon: Icons.trending_up,
                  label: 'Invest',
                  color: Colors.orange,
                ),
                SizedBox(width: 12),
                _buildActionButton(
                  icon: Icons.add,
                  label: 'Top up',
                  color: Colors.green,
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildActionButton({
    required IconData icon,
    required String label,
    required Color color,
  }) {
    return Expanded(
      child: Column(
        children: [
          Container(
            width: 44, 
            height: 44,
            decoration: BoxDecoration(
              color: color.withOpacity(0.15),
              shape: BoxShape.circle,
            ),
            child: Icon(
              icon,
              color: color,
              size: 22,
            ),
          ),
          SizedBox(height: 6),
          Text(
            label,
            style: TextStyle(
              color: Colors.grey[700],
              fontSize: 12,
              fontWeight: FontWeight.w500,
            ),
          ),
        ],
      ),
    );
  }
}