//
// Created by Roei Bracha on 26/12/2021.
//

#ifndef CLIENT_ENCODER_H
#define CLIENT_ENCODER_H

#include<vector>
#include <string>
#include <iostream>
#include <map>
#include <iomanip>
#include <ctime>
#include <sstream>

class encoder {
    public:
    encoder();
    static std::map<std::string, short> stringToOpcodeMap;


    static std::string encodeToBytes(std::string message);

    static void SplitString(std::string s, std::vector<std::string> &v);
    static void shortToBytes(short num, char *bytesArr);
    static void toUpperCase(std::string &str);
    static std::string addOpCodeToAns(std::string ans, short opCode);
    static std::string encodeRegister(std::vector<std::string> args);
    static std::string encodeLogin(std::vector<std::string> args);
    static std::string encodeLogout(std::vector<std::string> args);
    static std::string encodeFollow(std::vector<std::string> args);
    static std::string encodePost(std::vector<std::string> args);
    static std::string encodePM(std::vector<std::string> args);
    static std::string encodeLogStat(std::vector<std::string> args);
    static std::string encodeStat(std::vector<std::string> args);
    static std::string encodeBlock(std::vector<std::string> args);
};


#endif //CLIENT_ENCODER_H
