package com.iplizi.firstcomposeapp

data class ListItem(
    val title: String,
    val isSelected: Boolean
)

data class RequestListItem(
    val title: String,
    val description: String
)
