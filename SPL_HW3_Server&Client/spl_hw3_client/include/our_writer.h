//
// Created by Roei Bracha on 28/12/2021.
//

#ifndef CLIENT_OUR_WRITER_H
#define CLIENT_OUR_WRITER_H

#include <iostream>
#include "connectionHandler.h"
#include "main.h"


extern bool exit_thread_flag;
extern bool to_wait_flag;

class our_writer {

public:
    our_writer(ConnectionHandler &connectionHandler);
    void run();

private:

    ConnectionHandler &connectionHandler;
};


#endif //CLIENT_OUR_WRITER_H
