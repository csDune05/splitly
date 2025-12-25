import 'package:flutter/material.dart';
import '../src/global_styles.dart';
import '../src/bouncy_button.dart';

enum GroupStatus { active, paymentProcess, done }

class GroupModel {
  final String id;
  final String name;
  final String? avatarUrl;
  final GroupStatus status;
  final List<String> memberAvatars; 
  final int totalMemberCount; 
  final Color? avatarColor;

  GroupModel({
    required this.id,
    required this.name,
    this.avatarUrl,
    required this.status,
    List<String>? memberAvatars, 
    this.totalMemberCount = 0, 
    this.avatarColor,
  }) : memberAvatars = memberAvatars ?? [];

  int get memberCount => totalMemberCount;
  
  List<String> get displayAvatars => memberAvatars.take(4).toList();
}

class GroupCard extends StatelessWidget {
  final GroupModel group;
  final VoidCallback? onTap;

  const GroupCard({
    Key? key,
    required this.group,
    this.onTap,
  }) : super(key: key);

  String _getStatusText(GroupStatus status) {
    switch (status) {
      case GroupStatus.active:
        return 'Active';
      case GroupStatus.paymentProcess:
        return 'Bill Sharing';
      case GroupStatus.done:
        return 'Done';
    }
  }

  Color _getStatusTextColor(GroupStatus status) {
    switch (status) {
      case GroupStatus.active:
        return AppColors.active; 
      case GroupStatus.paymentProcess:
        return AppColors.onProcess; 
      case GroupStatus.done:
        return AppColors.done;
    }
  }

  Color _getStatusBackgroundColor(GroupStatus status) {
    switch (status) {
      case GroupStatus.active:
        return AppColors.active.withOpacity(0.1); 
      case GroupStatus.paymentProcess:
        return AppColors.onProcess.withOpacity(0.1); 
      case GroupStatus.done:
        return AppColors.done.withOpacity(0.1);
    }
  }

  Color _getStatusBorderColor(GroupStatus status) {
    switch (status) {
      case GroupStatus.active:
        return AppColors.active.withOpacity(0.3); 
      case GroupStatus.paymentProcess:
        return AppColors.onProcess.withOpacity(0.3); 
      case GroupStatus.done:
        return AppColors.done.withOpacity(0.3);
    }
  }

  Widget _buildMemberAvatars() {
    if (group.memberAvatars.isEmpty) {
      return SizedBox.shrink();
    }

    if (group.memberAvatars.length == 1) {
      return Container(
        width: 32,
        height: 32,
        decoration: BoxDecoration(
          shape: BoxShape.circle,
          border: Border.all(color: Colors.white, width: 2),
        ),
        child: ClipOval(
          child: group.memberAvatars[0].isNotEmpty
              ? Image.asset(
                  group.memberAvatars[0],
                  fit: BoxFit.cover,
                  errorBuilder: (context, error, stackTrace) {
                    return Container(
                      color: Colors.grey[300],
                      child: Icon(Icons.person, color: Colors.white, size: 16),
                    );
                  },
                )
              : Container(
                  color: Colors.grey[300],
                  child: Icon(Icons.person, color: Colors.white, size: 16),
                ),
        ),
      );
    }

    List<Widget> avatarWidgets = [];
    List<String> displayAvatars = group.displayAvatars;
    int displayCount = displayAvatars.length > 4 ? 3 : displayAvatars.length;

    for (int i = 0; i < displayCount; i++) {
      avatarWidgets.add(
        Positioned(
          left: i * 20.0,
          child: Container(
            width: 32,
            height: 32,
            decoration: BoxDecoration(
              shape: BoxShape.circle,
              border: Border.all(color: Colors.white, width: 2),
            ),
            child: ClipOval(
              child: displayAvatars[i].isNotEmpty
                  ? Image.asset(
                      displayAvatars[i],
                      fit: BoxFit.cover,
                      errorBuilder: (context, error, stackTrace) {
                        return Container(
                          color: Colors.grey[300],
                          child: Icon(Icons.person, color: Colors.white, size: 16),
                        );
                      },
                    )
                  : Container(
                      color: Colors.grey[300],
                      child: Icon(Icons.person, color: Colors.white, size: 16),
                    ),
            ),
          ),
        ),
      );
    }

    if (group.totalMemberCount > 4) {
      avatarWidgets.add(
        Positioned(
          left: displayCount * 20.0,
          child: Container(
            width: 32,
            height: 32,
            decoration: BoxDecoration(
              shape: BoxShape.circle,
              color: Colors.grey[400],
              border: Border.all(color: Colors.white, width: 2),
            ),
            child: Center(
              child: Text(
                '+${group.totalMemberCount - 4}', 
                style: TextStyle(
                  color: Colors.white,
                  fontSize: 10,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
          ),
        ),
      );
      displayCount++;
    }

    return Container(
      width: (displayCount * 20.0) + 12, 
      height: 32,
      child: Stack(
        children: avatarWidgets,
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.only(bottom: 16),
      child: BouncyButton(
        onTap: onTap,
        child: Container(
          padding: EdgeInsets.all(20),
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(20),
            boxShadow: [
              BoxShadow(
                color: Colors.grey.withOpacity(0.12),
                spreadRadius: 0,
                blurRadius: 12,
                offset: Offset(0, 6),
              ),
              BoxShadow(
                color: Colors.grey.withOpacity(0.06),
                spreadRadius: 0,
                blurRadius: 3,
                offset: Offset(0, 2),
              ),
            ],
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                group.name,
                style: AppTextStyles.groupTitle,
                maxLines: 2,
                overflow: TextOverflow.ellipsis,
              ),
              
              SizedBox(height: 12),

              Row(
                children: [
                  Icon(
                    Icons.people_outline,
                    size: 20,
                    color: Colors.grey[600],
                  ),
                  SizedBox(width: 4),
                  Text(
                    '${group.memberCount} members',
                    style: AppTextStyles.caption,
                  ),
                ],
              ),
              
              SizedBox(height: 16),
              
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Container(
                    width: 200,
                    padding: EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                    decoration: BoxDecoration(
                      color: _getStatusBackgroundColor(group.status),
                      borderRadius: BorderRadius.circular(20),
                      border: Border.all(
                        color: _getStatusBorderColor(group.status),
                        width: 1,
                      ),
                    ),
                    child: Center(
                      child: Text(
                        _getStatusText(group.status),
                        style: TextStyle(
                          fontSize: 14,
                          fontWeight: FontWeight.w700,
                          color: _getStatusTextColor(group.status),
                        ),
                      ),
                    ),
                  ),
                  
                  _buildMemberAvatars(),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}