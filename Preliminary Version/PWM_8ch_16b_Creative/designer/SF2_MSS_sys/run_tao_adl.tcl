set_family {SmartFusion2}
read_adl {C:\Users\Hiperwall\Desktop\NewProject\PWM_8ch_16b_Creative\designer\SF2_MSS_sys\SF2_MSS_sys.adl}
map_netlist
read_sdc {C:\Users\Hiperwall\Desktop\NewProject\PWM_8ch_16b_Creative\constraint\SF2_MSS_sys_derived_constraints.sdc}
check_constraints {C:\Users\Hiperwall\Desktop\NewProject\PWM_8ch_16b_Creative\constraint\placer_sdc_errors.log}
write_sdc -strict {C:\Users\Hiperwall\Desktop\NewProject\PWM_8ch_16b_Creative\designer\SF2_MSS_sys\place_route.sdc}
