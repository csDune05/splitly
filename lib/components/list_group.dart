import 'package:flutter/material.dart';
import '../src/global_styles.dart';
import '../src/screens.dart';
import 'group_card.dart';

enum FilterOption { all, active, paymentProcess, done }

class ListGroup extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _ListGroupState();
}

class _ListGroupState extends State<ListGroup> {
  FilterOption _currentFilter = FilterOption.all;
  List<GroupModel> _allGroups = [];

  List<GroupModel> get _filteredGroups {
    if (_currentFilter == FilterOption.all) {
      return _allGroups;
    }
    
    List<GroupModel> filtered = [];
    for (var group in _allGroups) {
      bool shouldInclude = false;
      
      if (_currentFilter == FilterOption.active && group.status == GroupStatus.active) {
        shouldInclude = true;
      } else if (_currentFilter == FilterOption.paymentProcess && group.status == GroupStatus.paymentProcess) {
        shouldInclude = true;
      } else if (_currentFilter == FilterOption.done && group.status == GroupStatus.done) {
        shouldInclude = true;
      }
      
      if (shouldInclude) {
        filtered.add(group);
      }
    }
    
    return filtered;
  }

  @override
  void initState() {
    super.initState();
    _createGroups();
  }

  void _createGroups() {
    final group1 = GroupModel(
      id: 'group_1',
      name: 'Nha Trang trip',
      status: GroupStatus.paymentProcess,
      memberAvatars: [
        'assets/images/user_avatar.jpg',
        'assets/images/user2_avatar.jpg',
        'assets/images/user3_avatar.jpg',
        'assets/images/user4_avatar.jpg',
      ],
      totalMemberCount: 4, 
    );
    _allGroups.add(group1);

    final group2 = GroupModel(
      id: 'group_2',
      name: 'Breakfast at school',
      status: GroupStatus.active,
      memberAvatars: [
        'assets/images/user_avatar.jpg', 
        'assets/images/user3_avatar.jpg',
      ],
      totalMemberCount: 2, 
    );
    _allGroups.add(group2);

    final group3 = GroupModel(
      id: 'group_3',
      name: 'Soc Son Camping',
      status: GroupStatus.done,
      memberAvatars: [
        'assets/images/user4_avatar.jpg',
        'assets/images/user3_avatar.jpg',
        'assets/images/user2_avatar.jpg',
        'assets/images/user_avatar.jpg',
      ],
      totalMemberCount: 12,
    );
    _allGroups.add(group3);

    final group4 = GroupModel(
      id: 'group_4',
      name: 'Weekend Hiking',
      status: GroupStatus.done,
      memberAvatars: [
        'assets/images/user2_avatar.jpg',
        'assets/images/user4_avatar.jpg',
        'assets/images/user_avatar.jpg',
        'assets/images/user3_avatar.jpg',
      ],
      totalMemberCount: 5,
    );
    _allGroups.add(group4);

    setState(() {});
  }

  String _getFilterText(FilterOption filter) {
    if (filter == FilterOption.all) {
      return 'All Groups';
    } else if (filter == FilterOption.active) {
      return 'Active';
    } else if (filter == FilterOption.paymentProcess) {
      return 'Payment Process';
    } else if (filter == FilterOption.done) {
      return 'Done';
    }
    return 'All Groups';
  }

  void _onGroupTap(GroupModel group) {
    // TODO group detail
  }

  void _showFilterOptions() {
    showModalBottomSheet(
      context: context,
      backgroundColor: Colors.transparent,
      builder: (context) => Container(
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.only(
            topLeft: Radius.circular(24),
            topRight: Radius.circular(24),
          ),
        ),
        padding: EdgeInsets.symmetric(vertical: 24, horizontal: 20),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Center(
              child: Container(
                width: 40,
                height: 4,
                decoration: BoxDecoration(
                  color: Colors.grey[300],
                  borderRadius: BorderRadius.circular(2),
                ),
              ),
            ),
            SizedBox(height: 20),
            
            Text(
              'View Groups By',
              style: TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.bold,
                color: Colors.grey[800],
              ),
            ),
            SizedBox(height: 16),
            
            _buildFilterOption(FilterOption.all),
            
            _buildFilterOption(FilterOption.active),
            
            _buildFilterOption(FilterOption.paymentProcess),
            
            _buildFilterOption(FilterOption.done),
            
            SizedBox(height: MediaQuery.of(context).padding.bottom),
          ],
        ),
      ),
    );
  }

  Widget _buildFilterOption(FilterOption filter) {
    final isSelected = _currentFilter == filter;
    
    return Container(
      margin: EdgeInsets.only(bottom: 8),
      child: BouncyButton(
        onTap: () {
          setState(() {
            _currentFilter = filter;
          });
          Navigator.pop(context);
        },
        child: Container(
          padding: EdgeInsets.symmetric(vertical: 16, horizontal: 16),
          decoration: BoxDecoration(
            color: isSelected ? AppColors.primary.withOpacity(0.1) : Colors.transparent,
            borderRadius: BorderRadius.circular(12),
            border: Border.all(
              color: isSelected ? AppColors.primary : Colors.grey.shade200,
              width: 1.5,
            ),
          ),
          child: Row(
            children: [
              Icon(
                isSelected ? Icons.check_circle : Icons.radio_button_unchecked,
                color: isSelected ? AppColors.primary : Colors.grey[400],
                size: 20,
              ),
              SizedBox(width: 12),
              Text(
                _getFilterText(filter),
                style: TextStyle(
                  fontSize: 16,
                  fontWeight: isSelected ? FontWeight.w600 : FontWeight.w500,
                  color: isSelected ? AppColors.primary : Colors.grey[700],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return SliverToBoxAdapter(
      child: Container(
        padding: EdgeInsets.symmetric(horizontal: 20, vertical: 12),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Padding(
              padding: EdgeInsets.symmetric(vertical: 12),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  Text(
                    ' Groups',
                    style: TextStyle(
                      fontSize: 20,
                      color: AppColors.primary,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  BouncyButton(
                    onTap: _showFilterOptions,
                    child: Container(
                      padding: EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                      decoration: BoxDecoration(
                        color: AppColors.primary.withOpacity(0.1),
                        borderRadius: BorderRadius.circular(20),
                        border: Border.all(
                          color: AppColors.primary.withOpacity(0.3),
                          width: 1,
                        ),
                      ),
                      child: Row(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          Text(
                            'View by',
                            style: TextStyle(
                              color: AppColors.primary,
                              fontSize: 14,
                              fontWeight: FontWeight.w600,
                            ),
                          ),
                          SizedBox(width: 4),
                          Icon(
                            Icons.expand_more,
                            color: AppColors.primary,
                            size: 16,
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            ),
            
            if (_currentFilter != FilterOption.all) 
              Container(
                margin: EdgeInsets.only(bottom: 16),
                padding: EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                decoration: BoxDecoration(
                  color: Colors.grey[100],
                  borderRadius: BorderRadius.circular(16),
                ),
                child: Text(
                  'Showing: ${_getFilterText(_currentFilter)} (${_filteredGroups.length})',
                  style: AppTextStyles.caption,
                ),
              ),
            
            SizedBox(height: 4),
            
            for (int i = 0; i < _filteredGroups.length; i++)
              GroupCard(
                group: _filteredGroups[i],
                onTap: () => _onGroupTap(_filteredGroups[i]),
              ),
            
            if (_filteredGroups.isEmpty)
              Container(
                margin: EdgeInsets.only(top: 40),
                child: Center(
                  child: Column(
                    children: [
                      Icon(
                        Icons.group_sharp,
                        size: 64,
                        color: Colors.grey[300],
                      ),
                      SizedBox(height: 16),
                      Text(
                        'No groups found',
                        style: TextStyle(
                          fontSize: 18,
                          fontWeight: FontWeight.w600,
                          color: Colors.grey[500],
                        ),
                      ),
                      SizedBox(height: 8),
                      Text(
                        'Try changing the filter to see more groups',
                        style: TextStyle(
                          fontSize: 14,
                          color: Colors.grey[400],
                        ),
                      ),
                    ],
                  ),
                ),
              ),
          ],
        ),
      ),
    );
  }
}