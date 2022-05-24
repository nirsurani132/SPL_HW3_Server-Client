//
// Created by Roei Bracha on 28/12/2021.
//

#include "our_writer.h"

extern bool to_wait_flag;

our_writer::our_writer(ConnectionHandler &connectionHandler) : connectionHandler(connectionHandler) {}

void our_writer::run() {
    extern bool exit_thread_flag;
    while (!exit_thread_flag) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        if (!connectionHandler.sendLine(line)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
    }
}