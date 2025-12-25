import 'package:flutter/material.dart';
import 'global_styles.dart';
import '../components/navigation_bar.dart';

class FriendPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _FriendState();
}

class _FriendState extends State<FriendPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: Text("friends"),
      ),

      bottomNavigationBar: HomeNavigationBar(
        onAddTap: (){},
        currentRoute: '/group',
      ),
    );
  }
}