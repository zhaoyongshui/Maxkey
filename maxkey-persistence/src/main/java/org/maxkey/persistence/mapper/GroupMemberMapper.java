/*
 * Copyright [2020] [MaxKey of copyright http://www.maxkey.top]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 

/**
 * 
 */
package org.maxkey.persistence.mapper;

import java.util.List;

import org.apache.mybatis.jpa.persistence.IJpaBaseMapper;
import org.maxkey.entity.GroupMember;
import org.maxkey.entity.Groups;
import org.maxkey.entity.UserInfo;

/**
 * @author Crystal.sea
 *
 */

public  interface GroupMemberMapper extends IJpaBaseMapper<GroupMember> {
	
	public List<GroupMember> allMemberInGroup(GroupMember entity);
	public List<GroupMember> memberInGroup(GroupMember entity);
	public List<GroupMember> memberNotInGroup(GroupMember entity);
	public List<GroupMember> groupMemberInGroup(GroupMember entity);
	
	public int addDynamicGroupMember(Groups dynamicGroup);
	
	public int deleteDynamicGroupMember(Groups dynamicGroup);
	
	public int deleteByGroupId(String groupId);
	
	public List<UserInfo> queryMemberByGroupId(String groupId);
	
	
	
}
