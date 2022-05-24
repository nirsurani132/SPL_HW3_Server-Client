//
// Created by Roei Bracha on 26/12/2021.
//

#include "encoder.h"

void encoder::toUpperCase(std::string &str) {
    for (int i = 0; i < (int)str.length(); i++) {
        str[i] = toupper(str[i]);
    }
}


std::map<std::string, short> encoder::stringToOpcodeMap = {
        {"REGISTER",     1},
        {"LOGIN",        2},
        {"LOGOUT",       3},
        {"FOLLOW",       4},
        {"POST",         5},
        {"PM",           6},
        {"LOGSTAT",      7},
        {"STAT",         8},
        {"NOTIFICATION", 9},
        {"ACK",          10},
        {"ERROR",        11},
        {"BLOCK",       12},
};

void encoder::shortToBytes(short num, char *bytesArr) {
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}

void encoder::SplitString(std::string s, std::vector<std::string> &v) {
    std::string temp = "";
    for (int i = 0; i < (int)s.length(); ++i) {
        if (s[i] == ' ') {
            v.push_back(temp);
            temp = "";
        } else {
            temp.push_back(s[i]);
        }

    }
    v.push_back(temp);
}

std::string encoder::encodeToBytes(std::string message) {
    std::vector<std::string> v;
    SplitString(message, v);
    toUpperCase(v[0]);
    short opCode = stringToOpcodeMap.at(v[0]);
    switch (opCode) {
        case 1:
            return encodeRegister(v);
        case 2:
            return encodeLogin(v);
        case 3:
            return encodeLogout(v);
        case 4:
            return encodeFollow(v);
        case 5:
            return encodePost(v);
        case 6:
            return encodePM(v);
        case 7:
            return encodeLogStat(v);
        case 8:
            return encodeStat(v);
        case 12:
            return encodeBlock(v);
    }
    return nullptr;
}

std::string encoder::addOpCodeToAns(std::string ans, short opCode) {
    char opCodeBytes[2];
    shortToBytes(opCode, opCodeBytes);
    for (char byte: opCodeBytes) {
        ans = ans + (char) byte;
    }
    return ans;
}

std::string encoder::encodeRegister(std::vector<std::string> args) {
    std::string ans = addOpCodeToAns("", 1);
    for (int i = 1; i < (int)args.size(); ++i) {
        ans += args[i];
        ans += (char) '\0';
    }
    return ans;
}

std::string encoder::encodeLogin(std::vector<std::string> args) {
    std::string ans = addOpCodeToAns("", 2);
    for (int i = 1; i < (int)args.size(); ++i) {
        ans += args[i];
        ans += (char) '\0';
    }
    ans.pop_back(); // remove the last \0
    return ans;
}

std::string encoder::encodeLogout(std::vector<std::string> args) {
    return addOpCodeToAns("", 3);
}

std::string encoder::encodeFollow(std::vector<std::string> args) {
    std::string ans = addOpCodeToAns("", 4);
    ans += args[1];
    ans += args[2];
    return ans;
}

std::string encoder::encodePost(std::vector<std::string> args) {
    std::string ans = addOpCodeToAns("", 5);
    ans += args[1];
    for (int i = 2; i < (int)args.size(); ++i) {
        ans += " "+ args[i];
    }
    ans += '\0';
    return ans;
}

std::string encoder::encodePM(std::vector<std::string> args) {
    std::string ans = addOpCodeToAns("", 6);
    ans += args[1];
    ans += '\0';
    ans += args[2];
    for (int i = 3; i < (int)args.size(); ++i) {
        ans += " "+ args[i];
    }
    ans += '\0';
    auto t = std::time(nullptr);
    auto tm = *std::localtime(&t);
    std::ostringstream oss;
    oss << std::put_time(&tm, "%d-%m-%Y %H-%M");
    ans += oss.str();
    ans += '\0';
    return ans;
}

std::string encoder::encodeLogStat(std::vector<std::string> args) {
    return addOpCodeToAns("", 7);
}

std::string encoder::encodeStat(std::vector<std::string> args) {
    std::string ans = addOpCodeToAns("", 8);
    for (int i = 1; i < (int)args.size(); ++i) {
        ans += args[i];
        ans += (char) '\0';
    }
    return ans;
}


std::string encoder::encodeBlock(std::vector<std::string> args) {
    std::string ans = addOpCodeToAns("", 12);
    ans += args[1];
    ans += '\0';
    return ans;
}