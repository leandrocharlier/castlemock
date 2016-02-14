/*
 * Copyright 2015 Karl Dahlgren
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortmocks.web.mock.rest.model.project.service;

import com.fortmocks.core.basis.model.Repository;
import com.fortmocks.core.basis.model.ServiceResult;
import com.fortmocks.core.basis.model.ServiceTask;
import com.fortmocks.core.mock.rest.model.project.domain.*;
import com.fortmocks.core.mock.rest.model.project.dto.RestApplicationDto;
import com.fortmocks.core.mock.rest.model.project.dto.RestMethodDto;
import com.fortmocks.core.mock.rest.model.project.dto.RestProjectDto;
import com.fortmocks.core.mock.rest.model.project.dto.RestResourceDto;
import com.fortmocks.core.mock.rest.model.project.service.message.input.CreateRestApplicationInput;
import com.fortmocks.core.mock.rest.model.project.service.message.input.CreateRestMethodInput;
import com.fortmocks.core.mock.rest.model.project.service.message.output.CreateRestApplicationOutput;
import com.fortmocks.core.mock.rest.model.project.service.message.output.CreateRestMethodOutput;
import com.fortmocks.web.mock.rest.model.project.RestApplicationDtoGenerator;
import com.fortmocks.web.mock.rest.model.project.RestMethodDtoGenerator;
import com.fortmocks.web.mock.rest.model.project.RestProjectDtoGenerator;
import com.fortmocks.web.mock.rest.model.project.RestResourceDtoGenerator;
import org.dozer.DozerBeanMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.ArrayList;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */
public class CreateRestMethodServiceTest {

    @Spy
    private DozerBeanMapper mapper;

    @Mock
    private Repository repository;

    @InjectMocks
    private CreateRestMethodService service;

    private RestProject restProject = RestProjectDtoGenerator.generateRestProject();
    private RestApplication restApplication = RestApplicationDtoGenerator.generateRestApplication();
    private RestResource restResource = RestResourceDtoGenerator.generateRestResource();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        restProject.getApplications().add(restApplication);
        restApplication.getResources().add(restResource);
        Mockito.when(repository.findOne(Mockito.anyString())).thenReturn(restProject);
    }

    @Test
    public void testProcess(){
        final RestMethodDto restMethodDto = RestMethodDtoGenerator.generateRestMethodDto();

        final CreateRestMethodInput input = new CreateRestMethodInput(restProject.getId(), restApplication.getId(), restResource.getId(), restMethodDto);
        final ServiceTask<CreateRestMethodInput> serviceTask = new ServiceTask<CreateRestMethodInput>(input);
        final ServiceResult<CreateRestMethodOutput> serviceResult = service.process(serviceTask);
        final CreateRestMethodOutput createRestApplicationOutput = serviceResult.getOutput();
        final RestMethodDto returnedRestMethod = createRestApplicationOutput.getCreatedRestMethod();

        Assert.assertEquals(restMethodDto.getName(), returnedRestMethod.getName());
        Assert.assertEquals(restMethodDto.getHttpMethod(), returnedRestMethod.getHttpMethod());
        Assert.assertEquals(restMethodDto.getDefaultBody(), returnedRestMethod.getDefaultBody());
        Assert.assertEquals(restMethodDto.getForwardedEndpoint(), returnedRestMethod.getForwardedEndpoint());
        Assert.assertEquals(restMethodDto.getStatus(), returnedRestMethod.getStatus());
        Assert.assertEquals(restMethodDto.getMockResponses(), returnedRestMethod.getMockResponses());
        Assert.assertEquals(restMethodDto.getResponseStrategy(), returnedRestMethod.getResponseStrategy());
    }

    @Test
    public void testProcessWithoutStatus(){
        final RestMethodDto restMethodDto = RestMethodDtoGenerator.generateRestMethodDto();
        restMethodDto.setStatus(null);

        final CreateRestMethodInput input = new CreateRestMethodInput(restProject.getId(), restApplication.getId(), restResource.getId(), restMethodDto);
        final ServiceTask<CreateRestMethodInput> serviceTask = new ServiceTask<CreateRestMethodInput>(input);
        final ServiceResult<CreateRestMethodOutput> serviceResult = service.process(serviceTask);
        final CreateRestMethodOutput createRestApplicationOutput = serviceResult.getOutput();
        final RestMethodDto returnedRestMethod = createRestApplicationOutput.getCreatedRestMethod();

        Assert.assertEquals(restMethodDto.getName(), returnedRestMethod.getName());
        Assert.assertEquals(restMethodDto.getHttpMethod(), returnedRestMethod.getHttpMethod());
        Assert.assertEquals(restMethodDto.getDefaultBody(), returnedRestMethod.getDefaultBody());
        Assert.assertEquals(restMethodDto.getForwardedEndpoint(), returnedRestMethod.getForwardedEndpoint());
        Assert.assertEquals(restMethodDto.getStatus(), RestMethodStatus.MOCKED);
        Assert.assertEquals(restMethodDto.getMockResponses(), returnedRestMethod.getMockResponses());
        Assert.assertEquals(restMethodDto.getResponseStrategy(), returnedRestMethod.getResponseStrategy());
    }

    @Test
    public void testProcessWithoutResponseStrategy(){
        final RestMethodDto restMethodDto = RestMethodDtoGenerator.generateRestMethodDto();
        restMethodDto.setResponseStrategy(null);

        final CreateRestMethodInput input = new CreateRestMethodInput(restProject.getId(), restApplication.getId(), restResource.getId(), restMethodDto);
        final ServiceTask<CreateRestMethodInput> serviceTask = new ServiceTask<CreateRestMethodInput>(input);
        final ServiceResult<CreateRestMethodOutput> serviceResult = service.process(serviceTask);
        final CreateRestMethodOutput createRestApplicationOutput = serviceResult.getOutput();
        final RestMethodDto returnedRestMethod = createRestApplicationOutput.getCreatedRestMethod();

        Assert.assertEquals(restMethodDto.getName(), returnedRestMethod.getName());
        Assert.assertEquals(restMethodDto.getHttpMethod(), returnedRestMethod.getHttpMethod());
        Assert.assertEquals(restMethodDto.getDefaultBody(), returnedRestMethod.getDefaultBody());
        Assert.assertEquals(restMethodDto.getForwardedEndpoint(), returnedRestMethod.getForwardedEndpoint());
        Assert.assertEquals(restMethodDto.getStatus(), returnedRestMethod.getStatus());
        Assert.assertEquals(restMethodDto.getMockResponses(), returnedRestMethod.getMockResponses());
        Assert.assertEquals(restMethodDto.getResponseStrategy(), RestResponseStrategy.RANDOM);
    }

}
