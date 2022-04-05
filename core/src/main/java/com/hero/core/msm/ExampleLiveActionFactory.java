//package com.hero.core.msm;
//
//import com.day.cq.wcm.api.WCMException;
//import com.day.cq.wcm.msm.api.LiveRelationship;
//import com.day.cq.wcm.msm.commons.BaseAction;
//import com.day.cq.wcm.msm.commons.BaseActionFactory;
//import org.apache.felix.scr.annotations.Service;
//import org.apache.felix.scr.annotations.Component;
//import org.apache.felix.scr.annotations.Property;
//
//import org.apache.sling.api.resource.Resource;
//import org.apache.sling.api.resource.ValueMap;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@Component(metatype = false)
//@Service
//public class ExampleLiveActionFactory extends BaseActionFactory<BaseAction> {
//
//    /**
//     * Live action name which must match node name under /msm/rolloutconfigs/yourolloutconfig/testAction
//     */
//
//    @Property(name = "liveActionName", propertyPrivate = true)
//    private static final String[] LIVE_ACTION_NAME = {ExampleLiveAction.class.getSimpleName(), "testAction"};
//
//    @Override
//    protected BaseAction newActionInstance(ValueMap valueMap) throws WCMException {
//        return new ExampleLiveAction(valueMap, this);
//    }
//
//    @Override
//    public String createsAction() {
//        return LIVE_ACTION_NAME[0];
//    }
//
//    /**
//     * Service Rollout action class.
//     */
//
//    private static final class ExampleLiveAction extends BaseAction {
//        private static final Logger log = LoggerFactory.getLogger(ExampleLiveAction.class);
//
//        ExampleLiveAction(ValueMap configuration, ExampleLiveActionFactory factory) {
//            super(configuration, factory);
//        }
//
//        @Override
//        protected boolean handles(Resource source, Resource target, LiveRelationship liveRelationship, boolean resetRollout) {
//            //method to check if this action handles this resource, in my case I had to check sling:resourceType
//            return false;
//        }
//
//        @Override
//        protected void doExecute(Resource source, Resource target, LiveRelationship liveRelationship, boolean resetRollout) {
//            //actual method to perform your custom actions
//            log.info("MSM customiszation Uraaaaaa!!!");
//        }
//    }
//}
