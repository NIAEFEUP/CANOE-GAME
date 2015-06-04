package com.example.duarte.canoefortwo.network;

import java.io.Serializable;

/**
 * Created by Duarte on 04/06/2015.
 */
public enum ClientServerMessages{
    CONNECT,
    DISCONNECT,
    TICK,
    CHOOSE_PLAYER_1,
    CHOOSE_PLAYER_2,
    FAILED,
    SUCCESS
}
