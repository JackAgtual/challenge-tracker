import { components } from "./api";

type ProblemDetail = components["schemas"]["ProblemDetail"];

export type ActionResponse =
  | {
      success: true;
      error?: undefined;
    }
  | {
      success: false;
      error: ProblemDetail;
    };

export type ActionResponseWithData<T> =
  | { success: true; data: T; error?: undefined }
  | {
      success: false;
      data?: undefined;
      error: ProblemDetail;
    };
