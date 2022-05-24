#include <stdlib.h>
#include <../include/connectionHandler.h>
#include <../include/our_writer.h>
#include <../include/our_listner.h>
#include "boost/thread/thread.hpp"
/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/

bool exit_thread_flag;
bool to_wait_flag;

int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);
    
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    exit_thread_flag = false;
    our_writer writer(connectionHandler);
    boost::thread   th1(&our_writer::run, &writer);
    our_listner listner(connectionHandler, th1);
    boost::thread   th2(&our_listner::run, &listner);
    th1.join();
    th2.join();
    return 0;
}
