import 'package:flutter/material.dart';

class ListGroup extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _ListGroupState();
}

class _ListGroupState extends State<ListGroup> {
  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: Row(
        children: [
          Text("Groups"),
        ],
      )
    );
  }
}