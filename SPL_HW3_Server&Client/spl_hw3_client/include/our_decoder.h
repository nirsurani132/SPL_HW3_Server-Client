//
// Created by Roei Bracha on 28/12/2021.
//

#ifndef CLIENT_OUR_DECODER_H
#define CLIENT_OUR_DECODER_H

#include<vector>
#include <string>
#include <iostream>
#include <map>

class our_decoder {
public:
    static std::map<short, std::string> opCodeToStringMap;
    static short bytesToShort(char* msg);
    static std::string decode(std::string msg);
    static std::string decodeAck(std::string msg);
    static std::string decodeError(std::string msg);
    static std::string decodeNotification(std::string msg);
};


#endif //CLIENT_OUR_DECODER_H
