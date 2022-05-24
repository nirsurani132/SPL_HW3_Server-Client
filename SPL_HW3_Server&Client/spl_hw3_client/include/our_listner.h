//
// Created by Roei Bracha on 28/12/2021.
//

#ifndef CLIENT_OUR_LISTNER_H
#define CLIENT_OUR_LISTNER_H
#include <iostream>
#include "connectionHandler.h"
#include "boost/thread.hpp"
#include "main.h"

class our_listner {
public:
    ConnectionHandler &connectionHandler;
    boost::thread& writeThread;
    our_listner(ConnectionHandler &connectionHandler, boost::thread   &writerThread);
    void run();
};


#endif //CLIENT_OUR_LISTNER_H
