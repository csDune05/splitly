import 'package:flutter/material.dart';
import 'global_styles.dart';
// import 'screens.dart';
import '../components/navigation_bar.dart';
import '../components/group_card.dart';

class GroupPage extends StatefulWidget {
  final GroupModel Group;

  GroupPage({
    required this.Group,
  });

  @override
  State<StatefulWidget> createState() => _GroupState();
}

class _GroupState extends State<GroupPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.Group.name), 
        titleTextStyle: AppTextStyles.whiteTitle,
        centerTitle: true,
        backgroundColor: AppColors.primary,
        flexibleSpace: Container(
          decoration: BoxDecoration(
            gradient: LinearGradient(
              colors: [
                AppColors.primary,
                AppColors.primaryLight,
                AppColors.accent,
              ],
              stops: [0.0, 0.7, 0.9],
              begin: Alignment.topLeft,
              end: Alignment.bottomRight,
            ),
          ),
        ),
        elevation: 0,
        leading: IconButton(
          icon: Icon(Icons.arrow_back_ios, color: Colors.white),
          onPressed: () => Navigator.pop(context),
        ),
      ),

      body: SafeArea(
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text("Group Page, ${widget.Group.name}"),
            ],
          ),
         
        )
      ),

      bottomNavigationBar: HomeNavigationBar(
        onAddTap: (){},
        currentRoute: '/group',
      ),
    );
  }

}