//
// Created by Roei Bracha on 28/12/2021.
//

#include "our_decoder.h"


std::map<short, std::string> our_decoder::opCodeToStringMap = {
        {1,  "REGISTER"},
        {2,  "LOGIN"},
        {3,  "LOGOUT"},
        {4,  "FOLLOW"},
        {5,  "POST"},
        {6,  "PM"},
        {7,  "LOGSTAT"},
        {8,  "STAT"},
        {9,  "NOTIFICATION"},
        {10, "ACK"},
        {11, "ERROR"},
        {12, "BLOCK"},
};


std::string our_decoder::decode(std::string message) {
    std::string ans = "";
    char opCodeChar[2] = {message[0], message[1]};
    short opCodeNum = bytesToShort(opCodeChar);
    ans += opCodeToStringMap.at(opCodeNum);
    ans += " ";
    switch (opCodeNum) {
        case 9:
            return ans + decodeNotification(message.substr(2));
        case 10:
            return ans + decodeAck(message.substr(2));
        case 11:
            return ans + decodeError(message.substr(2));
    }
    return nullptr;
}


short our_decoder::bytesToShort(char *bytesArr) {
    short result = (short) ((bytesArr[0] & 0xff) << 8);
    result += (short) (bytesArr[1] & 0xff);
    return result;
}

std::string our_decoder::decodeNotification(std::string message){
    std::string type = message[0] == '1' ? "Public" : "PM";
    std::string username = message.substr(1, message.find('\0')-1);
    message = message.substr(message.find('\0')+1);
    std::string content = message.substr(0,message.find('\0'));
    message = message.substr(message.find('\0')+1);
    return type + " " + username + " " + content + " " + message;
}

std::string our_decoder::decodeAck(std::string message){
    char opCodeChar[2] = {message[0], message[1]};
    short opCodeNum = bytesToShort(opCodeChar);
    if (opCodeNum != 8 && opCodeNum != 7)
        return std::to_string(opCodeNum) + message.substr(2);
    char ageChar[2] = {message[2], message[3]};
    char numPostsChar[2] = {message[4], message[5]};
    char numFollowersChar[2] = {message[6], message[7]};
    char numFollowingChar[2] = {message[8], message[9]};
    return std::to_string(opCodeNum) + " " + std::to_string(bytesToShort(ageChar)) + " " + std::to_string(bytesToShort(numPostsChar)) + " " + std::to_string(bytesToShort(numFollowersChar)) + " " + std::to_string(bytesToShort(numFollowingChar)) + " " + message.substr(10);
}

std::string our_decoder::decodeError(std::string message){
    char opCodeChar[2] = {message[0], message[1]};
    short opCodeNum = bytesToShort(opCodeChar);
    return std::to_string(opCodeNum) + message.substr(2);
}
