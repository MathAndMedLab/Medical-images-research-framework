# -*- coding: utf-8 -*-
# Implementation of Wang et al 2017: Automatic Brain Tumor Segmentation using Cascaded Anisotropic Convolutional Neural Networks. https://arxiv.org/abs/1709.00382

# Author: Guotai Wang
# Copyright (c) 2017-2018 University College London, United Kingdom. All rights reserved.
# http://cmictig.cs.ucl.ac.uk
#
# Distributed under the BSD-3 licence. Please see the file licence.txt
# This software is not certified for clinical use.
#
from __future__ import absolute_import, print_function

import numpy as np
import os
from util.data_process import *


class DataLoader():
    def __init__(self, config, data_root):
        """
        Initialize the calss instance
        inputs:
            config: a dictionary representing parameters
        """
        self.config = config
        self.data_root = data_root
        self.modality_postfix = config.get('modality_postfix', ['flair', 't1', 't1ce', 't2'])
        self.intensity_normalize = config.get('intensity_normalize', [True, True, True, True])
        self.with_ground_truth = config.get('with_ground_truth', False)
        self.label_convert_source = config.get('label_convert_source', None)
        self.label_convert_target = config.get('label_convert_target', None)
        self.label_postfix = config.get('label_postfix', 'seg')
        self.file_postfix = config.get('file_postfix', 'nii.gz')
        self.data_names = config.get('data_names', None)
        self.data_num = config.get('data_num', None)
        self.data_resize = config.get('data_resize', None)
        self.with_flip = config.get('with_flip', False)

        if (self.label_convert_source and self.label_convert_target):
            assert (len(self.label_convert_source) == len(self.label_convert_target))

    def __get_patient_names(self):
        """
        get the list of patient names, if self.data_names id not None, then load patient
        names from that file, otherwise search all the names automatically in data_root
        """
        # use pre-defined patient names
        if (self.data_names is not None):
            assert (os.path.isfile(self.data_names))
            with open(self.data_names) as f:
                content = f.readlines()
            patient_names = [x.strip() for x in content]
        return patient_names

    def __load_one_volume(self, patient_name, mod):
        patient_dir = patient_name
        # for bats17
        if ('nii' in self.file_postfix):
            image_names = os.listdir(patient_dir)
            volume_name = None
            for image_name in image_names:
                if (mod + '.' in image_name):
                    volume_name = image_name
                    break
        assert (volume_name is not None)
        volume_name = os.path.join(patient_dir, volume_name)
        volume = load_3d_volume_as_array(volume_name)
        return volume, volume_name

    def load_data(self, resize_3D_volume_to_given_shape=None):
        """
        load all the training/testing data
        """
        self.patient_names = self.__get_patient_names()
        assert (len(self.patient_names) > 0)
        ImageNames = []
        X = []
        W = []
        Y = []
        bbox = []
        in_size = []
        volume_list = []
        volume_name_list = []
        for mod_idx in range(len(self.modality_postfix)):
            volume, volume_name = self.__load_one_volume(self.data_root, self.modality_postfix[mod_idx])
            if (mod_idx == 0):
                margin = 5
                bbmin, bbmax = get_ND_bounding_box(volume, margin)
                volume_size = volume.shape
            volume = crop_ND_volume_with_bounding_box(volume, bbmin, bbmax)
            if (self.data_resize):
                volume = resize_3D_volume_to_given_shape(volume, self.data_resize, 1)
            if (mod_idx == 0):
                weight = np.asarray(volume > 0, np.float32)
            if (self.intensity_normalize[mod_idx]):
                volume = itensity_normalize_one_volume(volume)
            volume_list.append(volume)
            volume_name_list.append(volume_name)
        ImageNames.append(volume_name_list)
        X.append(volume_list)
        W.append(weight)
        bbox.append([bbmin, bbmax])
        in_size.append(volume_size)
        self.image_names = ImageNames
        self.data = X
        self.weight = W
        self.label = Y
        self.bbox = bbox
        self.in_size = in_size

    def get_total_image_number(self):
        """
        get the toal number of images
        """
        return len(self.data)

    def get_image_data_with_name(self, i):
        """
        Used for testing, get one image data and patient name
        """
        return [self.data[i], self.weight[i], self.patient_names[i], self.image_names[i], self.bbox[i], self.in_size[i]]
