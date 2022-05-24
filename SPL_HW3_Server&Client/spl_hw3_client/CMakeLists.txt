cmake_minimum_required(VERSION 3.21)
project(client)

set(CMAKE_CXX_STANDARD 11)

include_directories(include)
include_directories(src)

add_executable(client include/connectionHandler.h src/main.cpp src/connectionHandler.cpp src/encoder.cpp include/encoder.h src/our_decoder.cpp include/our_decoder.h src/our_listner.cpp include/our_listner.h src/our_writer.cpp include/our_writer.h include/main.h)

set(Boost_INCLUDE_DIR /opt/homebrew/Cellar/boost/)

find_package(Boost REQUIRED COMPONENTS system)
include_directories(${Boost_INCLUDE_DIRS})

target_link_libraries(${PROJECT_NAME} ${Boost_LIBRARIES})