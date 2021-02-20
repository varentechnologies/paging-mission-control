package sethkitchen;

/****************************************************************************************************************************
*    ____  ___   ___________   ________   __  _________________ ________  _   __   __________  _   ____________  ____  __ 
*   / __ \/   | / ____/  _/ | / / ____/  /  |/  /  _/ ___/ ___//  _/ __ \/ | / /  / ____/ __ \/ | / /_  __/ __ \/ __ \/ / 
*  / /_/ / /| |/ / __ / //  |/ / / __   / /|_/ // / \__ \\__ \ / // / / /  |/ /  / /   / / / /  |/ / / / / /_/ / / / / /  
* / ____/ ___ / /_/ // // /|  / /_/ /  / /  / // / ___/ /__/ // // /_/ / /|  /  / /___/ /_/ / /|  / / / / _, _/ /_/ / /___
* /_/   /_/  |_\____/___/_/ |_/\____/  /_/  /_/___//____/____/___/\____/_/ |_/   \____/\____/_/ |_/ /_/ /_/ |_|\____/_____/
*                                                                                                                         
* VERSION: 1
* HOME URL: https://github.com/SethKitchen/paging-mission-control
* KNOWN BUGS: none
*
* SYNOPSIS: Uses HashMap/Queue combination to efficiently keep track of satellites and timeframes
* while telemetry data comes in. Alerts when sensor reading is over threshold 3 times in 5 minutes.
*
* DESCRIPTION: You are tasked with assisting satellite ground operations for an earth science mission
* that monitors magnetic field variations at the Earth's poles. A pair of satellites fly in tandem orbit
* such that at least one will have line of sight with a pole to take accurate readings. The satellite’s
* science instruments are sensitive to changes in temperature and must be monitored closely.
* Onboard thermostats take several temperature readings every minute to ensure that the precision 
* magnetometers do not overheat. Battery systems voltage levels are also monitored to ensure that power is
* available to cooling coils. Design a monitoring and alert application that processes status telemetry from
* the satellites and generates alert messages in cases of certain limit violation scenarios.
* 
* If for the same satellite there are three battery voltage readings that are under the red low limit within
* a five minute interval.
*
* If for the same satellite there are three thermostat readings that exceed the red high limit within a five
* minute interval.
*
* COPYRIGHT 2021 Seth Kitchen <seth [AT] collaboarator [DOT] com>
*
*                       The Wide Open License (WOL)
*
* Permission to use, copy, modify, distribute and sell this software and its
* documentation for any purpose is hereby granted without fee, provided that
* the above copyright notice and this license appear in all source copies. 
* THIS SOFTWARE IS PROVIDED "AS IS" WITHOUT EXPRESS OR IMPLIED WARRANTY OF
* ANY KIND.
*
*****************************************************************************/

public class PagingMissionControl {
    public static void main(String[] args) {
        Runner runner=new Runner();
        runner.runProgram();
    }
}